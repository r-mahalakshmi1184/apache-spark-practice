package exercises

import org.apache.spark.sql.SparkSession

object Exercise09_ReduceVsReduceByKey {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 9 - reduce vs reduceByKey")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Plain RDD of numbers
    val numbers = sc.parallelize(Seq(10, 20, 30, 40, 50))

    // reduce: combines ALL elements into a single result
    val totalSum = numbers.reduce((a, b) => a + b)
    println("Plain RDD: " + numbers.collect().mkString(", "))
    println("reduce() result (single total): " + totalSum)

    // Pair RDD of (department, salary)
    val salaries = sc.parallelize(Seq(
      ("Engineering", 90000),
      ("Sales", 60000),
      ("Engineering", 95000),
      ("Marketing", 55000),
      ("Sales", 65000)
    ))

    // reduceByKey: combines elements PER KEY, returns a Pair RDD
    val totalByDept = salaries.reduceByKey((a, b) => a + b)

    println("\nPair RDD: " + salaries.collect().mkString(", "))
    println("reduceByKey() result (one total per key):")
    totalByDept.collect().sortBy(_._1).foreach { case (dept, total) =>
      println(s"$dept -> $total")
    }

    spark.stop()
  }
}
