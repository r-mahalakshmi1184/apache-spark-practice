# Exercise 4: RDD Immutability and Lineage

## Code
See `src/main/scala/exercises/Exercise04_RDDLineage.scala`

## Lineage (DAG)
numbers (original RDD: 1-10)
|
filter(_ % 2 == 0)
|
evens (2, 4, 6, 8, 10)
|
map(x => x * x)
|
squared (4, 16, 36, 64, 100)
|
reduce(_ + _)
|
total = 220

## Why the original RDD is not modified
RDDs are **immutable** — every transformation (filter, map, etc.) creates a
brand new RDD instead of changing the existing one. `numbers` still exists
unchanged after `filter` and `map` are applied; `evens` and `squared` are
separate RDD objects that simply point back to `numbers` in their lineage.

This lineage graph (visible via `toDebugString`) is also how Spark achieves
fault tolerance: if a partition is lost, Spark can recompute it by replaying
the transformations from the original data, instead of needing to store
copies of every intermediate RDD.
