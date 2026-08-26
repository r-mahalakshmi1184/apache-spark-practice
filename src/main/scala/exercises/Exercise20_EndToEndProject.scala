package exercises

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

object Exercise20_EndToEndProject {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 20 - End-to-End Real-Time Spark Project")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val outputPath = "data/output_transactions"
    import java.io.File
    def deleteRecursively(f: File): Unit = {
      if (f.isDirectory) f.listFiles().foreach(deleteRecursively)
      f.delete()
    }
    deleteRecursively(new File(outputPath))

    // 1. Input data: transaction_id, customer_id, amount, transaction_type, timestamp
   val transactions = Seq(
  (1, 101, Some(250.0), "debit", "2024-01-01 10:00:00"),
  (2, 102, Some(450.0), "credit", "2024-01-01 10:05:00"),
  (3, 101, None: Option[Double], "debit", "2024-01-01 10:10:00"), // null amount
  (4, 103, Some(300.0), "debit", "2024-01-01 10:15:00"),
  (5, 102, Some(150.0), "credit", "2024-01-01 10:20:00"),
  (6, 101, Some(500.0), "credit", "2024-01-01 10:25:00")
).toDF("transaction_id", "customer_id", "amount", "transaction_type", "timestamp")
  .withColumn("timestamp", to_timestamp($"timestamp"))

    println("Raw transactions:")
    transactions.show()

    // 2. Handle nulls - fill missing amount with 0
    val cleaned = transactions.na.fill(0.0, Seq("amount"))
    println("After handling nulls:")
    cleaned.show()

    // 3. UDF - categorize transaction size
    val categorize = udf((amount: Double) => {
      if (amount >= 400) "large"
      else if (amount >= 200) "medium"
      else "small"
    })
    val withCategory = cleaned.withColumn("size_category", categorize($"amount"))
    println("With size_category (UDF applied):")
    withCategory.show()

    // 4. Aggregate by customer
    val byCustomer = withCategory.groupBy("customer_id")
      .agg(
        sum("amount").as("total_amount"),
        count("*").as("transaction_count")
      )
    println("Aggregated by customer:")
    byCustomer.show()

    // 5. Window aggregation - running total per customer ordered by time
    val windowSpec = Window.partitionBy("customer_id").orderBy("timestamp")
    val withRunningTotal = withCategory.withColumn(
      "running_total", sum("amount").over(windowSpec)
    )
    println("Running total per customer (window function):")
    withRunningTotal.select("transaction_id", "customer_id", "amount", "running_total").show()

    // 6. Lookup DataFrame + broadcast join
    val customerLookup = Seq(
      (101, "Alice"),
      (102, "Bob"),
      (103, "Charlie")
    ).toDF("customer_id", "customer_name")

    val enriched = withRunningTotal.join(broadcast(customerLookup), Seq("customer_id"), "left")
    println("Enriched with customer name (broadcast join):")
    enriched.select("transaction_id", "customer_id", "customer_name", "amount", "running_total").show()

    // 7. Repartition before writing output
    val repartitioned = enriched.repartition(4, $"customer_id")
    println("Partitions before write: " + repartitioned.rdd.getNumPartitions)

    // 8. Write output to Parquet
    repartitioned.write.parquet(outputPath)
    println("Saved final output to: " + outputPath)

    spark.stop()
  }
}
