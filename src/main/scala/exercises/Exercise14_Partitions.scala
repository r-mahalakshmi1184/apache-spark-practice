package exercises

import org.apache.spark.sql.SparkSession

object Exercise14_Partitions {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 14 - Partitions and Default Parallelism")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Default parallelism - depends on available cores in local[*] mode
    println("Default parallelism: " + sc.defaultParallelism)

    // RDD created WITHOUT specifying partitions - uses default parallelism
    val defaultRDD = sc.parallelize(1 to 100)
    println("Partitions (default): " + defaultRDD.getNumPartitions)

    // RDD created WITH explicit partition count
    val customRDD = sc.parallelize(1 to 100, 6)
    println("Partitions (explicit = 6): " + customRDD.getNumPartitions)

    // Trigger a job and observe tasks (visible in Spark UI at localhost:4040)
    val sum = customRDD.map(_ * 2).reduce(_ + _)
    println("\nSum after processing: " + sum)
    println("This job ran with " + customRDD.getNumPartitions + " tasks (one per partition) in its stage.")

    spark.stop()
  }
}
