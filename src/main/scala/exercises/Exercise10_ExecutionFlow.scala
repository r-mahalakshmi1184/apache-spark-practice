package exercises

import org.apache.spark.sql.SparkSession

object Exercise10_ExecutionFlow {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 10 - Spark Execution Flow")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val numbers = sc.parallelize(1 to 20, 4)  // 4 partitions

    println("Number of partitions: " + numbers.getNumPartitions)

    // Narrow transformation
    val filtered = numbers.filter(_ % 2 == 0)

    // Wide transformation (creates a new stage due to shuffle)
    val pairs = filtered.map(x => (x % 3, x))
    val grouped = pairs.reduceByKey(_ + _)

    // Action - this triggers a Job
    val result = grouped.collect()

    println("\nResult (grouped by x % 3, summed):")
    result.sortBy(_._1).foreach { case (k, v) =>
      println(s"key=$k -> sum=$v")
    }

    println("\nCheck the Spark UI at http://localhost:4040 while running")
    println("to see this Job broken into Stages and Tasks.")

    Thread.sleep(30000)  // keep alive to inspect Spark UI

    spark.stop()
  }
}
