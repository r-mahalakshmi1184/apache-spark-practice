package exercises

import org.apache.spark.sql.SparkSession

object Exercise15_RepartitionVsCoalesce {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 15 - repartition vs coalesce")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Start with an RDD explicitly created with 8 partitions
    val original = sc.parallelize(1 to 100, 8)
    println("Original partitions: " + original.getNumPartitions)

    // coalesce: reduce to 3 partitions (no full shuffle - merges existing partitions)
    val coalesced = original.coalesce(3)
    println("After coalesce(3): " + coalesced.getNumPartitions)

    // repartition: increase to 12 partitions (full shuffle - redistributes all data)
    val repartitioned = original.repartition(12)
    println("After repartition(12): " + repartitioned.getNumPartitions)

    // Trigger jobs to see the difference in Spark UI (localhost:4040)
    val coalescedSum = coalesced.sum()
    val repartitionedSum = repartitioned.sum()

    println("\nSum via coalesced RDD: " + coalescedSum)
    println("Sum via repartitioned RDD: " + repartitionedSum)

    println("\nCheck Spark UI: coalesce(3) job should show minimal/no shuffle,")
    println("while repartition(12) job will show a shuffle stage.")

    spark.stop()
  }
}
