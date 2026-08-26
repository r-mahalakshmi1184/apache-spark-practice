# Exercise 5: Word Count Problem

## Code
See `src/main/scala/exercises/Exercise05_WordCount.scala`

## Steps: input → transformation → action

1. **Input**: An RDD of 4 sentences (each a String).

2. **Transformation - flatMap**: Splits each sentence into individual words
   and flattens all the resulting arrays into a single RDD of words.
   e.g. "Spark is fast" -> ["Spark", "is", "fast"]

3. **Transformation - map**: Converts each word into a (word, 1) key-value
   pair, so counting becomes a matter of summing values per key.
   e.g. "Spark" -> ("Spark", 1)

4. **Transformation - reduceByKey**: Groups pairs by key (word) and sums
   the values, giving the total occurrence count per word. This is a wide
   transformation - it triggers a shuffle since matching keys may live on
   different partitions.

5. **Action - collect**: Triggers the actual execution of the entire chain
   (flatMap -> map -> reduceByKey) and brings the final results back to
   the driver as an array.

## Result
| Word | Count |
|------|-------|
| Spark | 3 |
| is | 4 |
| distributed | 2 |
| Hadoop | 1 |
| fast | 1 |
| powerful | 1 |
