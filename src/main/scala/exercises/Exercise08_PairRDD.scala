package exercises

import org.apache.spark.sql.SparkSession

object Exercise08_PairRDD {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 8 - Understanding Pair RDDs")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Pair RDD: (key, value) tuples
    val languageScores = sc.parallelize(Seq(
      ("Java", 10),
      ("Spark", 20),
      ("Java", 15),
      ("Python", 25)
    ))

    println("Original Pair RDD: " + languageScores.collect().mkString(", "))

    // reduceByKey: combine values for each key
    val totals = languageScores.reduceByKey((a, b) => a + b)

    println("\nTotal value per language:")
    totals.collect().sortBy(_._1).foreach { case (lang, total) =>
      println(s"$lang -> $total")
    }

    // Some other common Pair RDD operations
    println("\nKeys: " + languageScores.keys.collect().mkString(", "))
    println("Values: " + languageScores.values.collect().mkString(", "))
    println("Sorted by key: " + languageScores.sortByKey().collect().mkString(", "))

    spark.stop()
  }
}
