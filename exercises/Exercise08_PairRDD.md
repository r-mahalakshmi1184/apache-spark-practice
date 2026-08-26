# Exercise 8: Understanding Pair RDDs

## Code
See `src/main/scala/exercises/Exercise08_PairRDD.scala`

## What is a Pair RDD
A Pair RDD is an RDD where each element is a (key, value) tuple. This
enables key-based operations that a plain RDD can't do - like `reduceByKey`,
`groupByKey`, `sortByKey`, `join`, and accessing `.keys` / `.values`
directly.

## reduceByKey result
| Language | Total |
|---|---|
| Java | 25 (10 + 15) |
| Spark | 20 |
| Python | 25 |

## Key operations demonstrated
- `reduceByKey` - combines all values sharing a key using the given function
- `.keys` - extracts just the keys as a new RDD
- `.values` - extracts just the values as a new RDD
- `sortByKey()` - sorts the Pair RDD by its key

## Why Pair RDDs matter
Most real-world aggregation problems (word counts, sales by region, scores
by category) map naturally onto key-value pairs. Pair RDDs give access to
a family of by-key operations that would otherwise require manual grouping
logic.
