package exercises

import org.apache.spark.sql.SparkSession

object Exercise03_FirstRDD {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 3 - First RDD Program")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Create RDD
    val numbers = sc.parallelize(Seq(10, 20, 30, 40, 50))

    // Transformation: double every number (lazy - nothing runs yet)
    val doubled = numbers.map(_ * 2)

    // Action: triggers actual computation
    val total = doubled.sum()

    println("Original numbers: " + numbers.collect().mkString(", "))
    println("Doubled numbers: " + doubled.collect().mkString(", "))
    println("Sum of doubled numbers: " + total)

    spark.stop()
  }
}
