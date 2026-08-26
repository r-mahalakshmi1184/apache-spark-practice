# Exercise 14: Partitions and Default Parallelism

## Code
See `src/main/scala/exercises/Exercise14_Partitions.scala`

## What is default parallelism
`sc.defaultParallelism` reflects how many partitions Spark will use by
default when one isn't explicitly specified. In `local[*]` mode, this
equals the number of CPU cores available on the machine, since each
core can process one task at a time.

## Partitions vs tasks
- **Partitions** are chunks of the data, distributed across the cluster
  (or across local threads in `local[*]` mode).
- **Tasks** are the units of work Spark schedules to process each
  partition - there is exactly **one task per partition** per stage.
- More partitions = more tasks = more parallelism, up to the number of
  available cores; beyond that, tasks simply queue up.

## How partitions affect execution
- **Too few partitions**: underutilizes available cores - some cores sit
  idle while others do all the work.
- **Too many small partitions**: adds scheduling overhead - the time
  spent managing many tiny tasks can outweigh the benefit of parallelism.
- Choosing partition count is a balance based on data size, cluster
  size, and the nature of the operation (especially before/after
  shuffles, where `reduceByKey`/`repartition` may change partition
  count implicitly or explicitly).
