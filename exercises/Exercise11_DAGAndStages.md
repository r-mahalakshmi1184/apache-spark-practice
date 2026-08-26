# Exercise 11: DAG and Stages

## Code
See `src/main/scala/exercises/Exercise11_DAGAndStages.scala`

## Pipeline

textFile -> flatMap -> filter -> map -> reduceByKey -> saveAsTextFile


## Classification

| Step | Operation | Type |
|---|---|---|
| textFile | Read input | Input (not a transformation) |
| flatMap | Split lines into words | Narrow |
| filter | Keep words longer than 2 chars | Narrow |
| map | Convert to (word, 1) pairs | Narrow |
| reduceByKey | Sum counts per word | **Wide (shuffle boundary)** |
| saveAsTextFile | Write results to disk | **Action** |

## DAG diagram

Stage 1 (narrow chain - no shuffle needed)
textFile -> flatMap -> filter -> map
|
v
[ SHUFFLE ] <-- boundary caused by reduceByKey
|
v
Stage 2
reduceByKey -> saveAsTextFile


## Number of stages
**2 stages total.** Everything before `reduceByKey` can be pipelined into
a single stage since they're all narrow transformations (no data movement
between partitions required). `reduceByKey` forces a shuffle, which always
creates a new stage boundary. The final `reduceByKey` and `saveAsTextFile`
run in the second stage.

## Final action
`saveAsTextFile` is the action that triggers the entire DAG to execute -
without it, all the transformations above would remain unevaluated.
