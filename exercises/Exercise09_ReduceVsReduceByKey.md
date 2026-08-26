# Exercise 9: reduce vs reduceByKey

## Code
See `src/main/scala/exercises/Exercise09_ReduceVsReduceByKey.scala`

## Key difference

| | reduce | reduceByKey |
|---|---|---|
| Works on | Plain RDD | Pair RDD (key, value) |
| Output | A single value | A new Pair RDD, one value per key |
| Type | Action | Transformation |
| Triggers execution immediately | Yes | No - it's lazy, needs a following action like collect() |

## reduce
`reduce` combines every element in the RDD into one final result using a
binary function. It is an **action** - it immediately returns a single
value to the driver (e.g. summing all numbers into one total).

## reduceByKey
`reduceByKey` operates on a Pair RDD and combines values **per key**,
producing a new Pair RDD rather than a single scalar. It is a
**transformation** - it stays lazy and only actually runs once an action
(like `collect`) is called on the result. Internally it also involves a
shuffle, since values for the same key may live on different partitions.

## Summary
Use `reduce` when you want one aggregated answer from an entire dataset.
Use `reduceByKey` when you want a separate aggregated answer per group/key.
