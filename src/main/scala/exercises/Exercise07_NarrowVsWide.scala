package exercises

import org.apache.spark.sql.SparkSession

object Exercise07_NarrowVsWide {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 7 - Narrow vs Wide Transformations")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val numbers = sc.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

    // Narrow transformation: map (each output partition depends on one input partition)
    val doubled = numbers.map(_ * 2)

    // Narrow transformation: filter (no data movement across partitions)
    val evens = doubled.filter(_ % 4 == 0)

    println("After map + filter (narrow transformations): " + evens.collect().mkString(", "))

    val pairs = sc.parallelize(Seq(
      ("Java", 10), ("Spark", 20), ("Java", 15), ("Python", 25), ("Spark", 5)
    ))

    // Wide transformation: reduceByKey (causes a shuffle - combines per key across partitions)
    val summed = pairs.reduceByKey(_ + _)
    println("\nreduceByKey result (wide transformation): " + summed.collect().mkString(", "))

    // Wide transformation: groupByKey (also causes a shuffle - groups all values per key)
    val grouped = pairs.groupByKey()
    println("\ngroupByKey result (wide transformation): " + grouped.collect().mkString(", "))

    println("\nPhysical plan for reduceByKey (shows shuffle):")
    println(summed.toDebugString)

    spark.stop()
  }
}
