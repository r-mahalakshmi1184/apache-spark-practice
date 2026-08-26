package exercises

import org.apache.spark.sql.SparkSession

object Exercise06_TransformationVsAction {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 6 - Transformation vs Action")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val numbers = sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

    println("Building transformations (nothing runs yet)...")
    val filtered = numbers.filter(_ % 2 == 0)   // transformation
    val mapped = filtered.map(_ * 10)           // transformation

    println("Calling an action now - this triggers execution:")
    val total = mapped.reduce(_ + _)            // action
    println("Sum: " + total)

    val countResult = mapped.count()            // action
    println("Count: " + countResult)

    val collected = mapped.collect()            // action
    println("Collected: " + collected.mkString(", "))

    spark.stop()
  }
}
