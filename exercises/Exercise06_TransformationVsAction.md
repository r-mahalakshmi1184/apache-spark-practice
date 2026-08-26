# Exercise 6: Transformation vs Action

## Code
See `src/main/scala/exercises/Exercise06_TransformationVsAction.scala`

## Classification

| Operation | Type | Notes |
|---|---|---|
| map | Transformation | Applies a function to each element |
| filter | Transformation | Keeps elements matching a condition |
| flatMap | Transformation | Maps then flattens results |
| reduceByKey | Transformation | Aggregates values per key (wide - causes shuffle) |
| reduce | Action | Aggregates all elements into one result |
| collect | Action | Brings all data back to the driver |
| count | Action | Returns number of elements |
| saveAsTextFile | Action | Writes RDD contents to storage |

## Which operations are lazy
All **transformations** (map, filter, flatMap, reduceByKey) are executed
lazily - Spark just records them as steps in a DAG without running anything.
Execution is only triggered once an **action** (reduce, collect, count,
saveAsTextFile) is called. This lets Spark optimize the full chain of
transformations before doing any real work, and avoid computing anything
that isn't actually needed for the final result.
