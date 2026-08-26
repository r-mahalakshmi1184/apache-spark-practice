package exercises

import org.apache.spark.sql.SparkSession

object Exercise13_Accumulator {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 13 - Accumulator Exercise")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val transactions = sc.parallelize(Seq(100, -50, 200, -20, 300))

    // Create an accumulator to count negative transactions
    val negativeCount = sc.longAccumulator("NegativeTransactionCount")

    // Use foreach (an action) - accumulator updates happen inside this action
    transactions.foreach { txn =>
      if (txn < 0) {
        negativeCount.add(1)
      }
    }

    println("Transactions: " + transactions.collect().mkString(", "))
    println("Number of negative transactions: " + negativeCount.value)

    spark.stop()
  }
}
