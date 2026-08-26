package exercises

import org.apache.spark.sql.SparkSession

object Exercise04_RDDLineage {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 4 - RDD Immutability and Lineage")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Original RDD
    val numbers = sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

    // Transformation 1: filter (keep even numbers)
    val evens = numbers.filter(_ % 2 == 0)

    // Transformation 2: map (square each number)
    val squared = evens.map(x => x * x)

    // Action: reduce (sum all values)
    val total = squared.reduce((a, b) => a + b)

    println("Original RDD: " + numbers.collect().mkString(", "))
    println("After filter (evens): " + evens.collect().mkString(", "))
    println("After map (squared): " + squared.collect().mkString(", "))
    println("Final reduced sum: " + total)

    // Show the lineage
    println("\nRDD Lineage (toDebugString):")
    println(squared.toDebugString)

    spark.stop()
  }
}
