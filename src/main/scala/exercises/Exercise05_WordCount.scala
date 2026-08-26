package exercises

import org.apache.spark.sql.SparkSession

object Exercise05_WordCount {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 5 - Word Count Problem")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Input: sentences
    val sentences = sc.parallelize(Seq(
      "Spark is fast",
      "Spark is distributed",
      "Hadoop is distributed",
      "Spark is powerful"
    ))

    // Transformation 1: flatMap - split each sentence into words
    val words = sentences.flatMap(sentence => sentence.split(" "))

    // Transformation 2: map - turn each word into a (word, 1) pair
    val wordPairs = words.map(word => (word, 1))

    // Transformation 3: reduceByKey - sum counts for each word
    val wordCounts = wordPairs.reduceByKey((a, b) => a + b)

    // Action: collect - trigger execution and bring results to driver
    val result = wordCounts.collect()

    println("Word counts:")
    result.sortBy(_._1).foreach { case (word, count) =>
      println(s"$word -> $count")
    }

    spark.stop()
  }
}
