# Exercise 10: Spark Execution Flow

## Code
See `src/main/scala/exercises/Exercise10_ExecutionFlow.scala`

## Components identified

| Component | Role in this program |
|---|---|
| Driver | Runs main(), builds the DAG, coordinates execution |
| Job | Triggered by the `collect()` action - one Job per action call |
| Stage | The Job splits into 2 Stages, separated by the shuffle boundary from `reduceByKey` |
| Task | Each Stage runs 4 Tasks - one per partition (we set 4 partitions) |
| Executor | Runs the Tasks in parallel using local CPU cores (`local[*]`) |

## Flow diagram

Driver (runs main, builds DAG)
|
v
Action called: collect()
|
v
Job created
|
v
Stage 1 (narrow: parallelize -> filter -> map)
| 4 Tasks (1 per partition) run on Executors
v
Shuffle (data movement for reduceByKey)
|
v
Stage 2 (reduceByKey -> collect)
| 4 Tasks run on Executors
v
Results returned to Driver


## Explanation
1. The **Driver** builds a logical DAG of transformations as the code runs,
   but nothing executes until the `collect()` action.
2. That action creates one **Job**.
3. The Job is split into **Stages** at shuffle boundaries - here,
   `reduceByKey` forces a shuffle, creating 2 stages.
4. Each Stage is broken into **Tasks**, one per partition (4 partitions -> 4
   tasks per stage).
5. **Executors** (in local mode, worker threads on this machine) run the
   Tasks in parallel and return results to the Driver.
