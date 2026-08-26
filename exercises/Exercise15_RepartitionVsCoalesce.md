# Exercise 15: repartition vs coalesce

## Code
See `src/main/scala/exercises/Exercise15_RepartitionVsCoalesce.scala`

## Comparison

| | coalesce | repartition |
|---|---|---|
| Direction | Best for decreasing partitions | Works for increasing or decreasing |
| Shuffle | Avoids a full shuffle - merges nearby partitions | Always performs a full shuffle |
| Cost | Cheaper - minimal data movement | More expensive - redistributes all data across the cluster |
| Result balance | Partitions may be uneven in size | Data is evenly redistributed |

## Execution behavior observed
- `coalesce(3)` on an 8-partition RDD merges existing partitions together
  without redistributing all the data - it's a narrow-ish operation that
  avoids a full shuffle where possible.
- `repartition(12)` always triggers a full shuffle, since it needs to
  redistribute data from 8 partitions into 12 new, evenly-sized
  partitions - this requires moving data around the whole cluster.

## When to use each
- Use **coalesce** when **reducing** the number of partitions, especially
  after a filter that dramatically shrinks the dataset (e.g. before
  writing output, to avoid many small output files) - it's cheaper
  because it avoids a full shuffle.
- Use **repartition** when you need to **increase** partitions, or when
  you need partitions to be evenly balanced (e.g. before a
  computationally heavy operation, to make sure work is spread evenly
  across all executors) - accepting the cost of a full shuffle.
