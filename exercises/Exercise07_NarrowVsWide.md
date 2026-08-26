# Exercise 7: Narrow vs Wide Transformations

## Code
See `src/main/scala/exercises/Exercise07_NarrowVsWide.scala`

## Classification

| Operation | Type | Shuffle? |
|---|---|---|
| map | Narrow | No - each output partition depends on exactly one input partition |
| filter | Narrow | No - data stays within its partition |
| reduceByKey | Wide | Yes - values for the same key may live on different partitions and must be shuffled together before aggregating |
| groupByKey | Wide | Yes - all values for a key must be brought together across partitions before grouping |

## Where the shuffle occurs
A shuffle happens whenever data with the same key needs to move between
partitions to be combined. In `reduceByKey` and `groupByKey`, records are
spread across partitions based on their original placement, but the
operation needs all values for a given key on the same partition — so
Spark redistributes (shuffles) the data across the cluster/network before
completing the operation.

This is visible in the debug output as a `ShuffledRDD` in the lineage.

## Narrow vs wide - practical impact
- Narrow transformations are cheap - no network I/O, can be pipelined together.
- Wide transformations are expensive - they involve disk I/O, network
  transfer, and often create a new "stage" boundary in the execution plan.
- `reduceByKey` is generally preferred over `groupByKey` when aggregating,
  since it combines values locally on each partition before shuffling,
  reducing the amount of data moved across the network.
