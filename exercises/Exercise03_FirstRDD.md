# Exercise 3: First RDD Program

## Code
See `src/main/scala/exercises/Exercise03_FirstRDD.scala`

## Steps
1. Create an RDD from a sequence: 10, 20, 30, 40, 50
2. Apply a transformation (`map`) to double every number
3. Apply an action (`sum`) to compute the total

## Why the transformation is lazily evaluated
Spark transformations like `map` don't execute immediately — they just build
up a logical plan (DAG) describing what should happen. Nothing actually runs
until an **action** (like `sum`, `collect`, or `count`) is called. This lets
Spark optimize the whole chain of transformations before running anything,
avoiding unnecessary computation and unnecessary passes over the data.
