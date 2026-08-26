package exercises

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Exercise18_SparkSQLDataFrame {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 18 - Spark SQL and DataFrame Operations")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val outputPath = "data/output_dept_avg_salary"

    // Clean up previous output if it exists
    import java.io.File
    def deleteRecursively(f: File): Unit = {
      if (f.isDirectory) f.listFiles().foreach(deleteRecursively)
      f.delete()
    }
    deleteRecursively(new File(outputPath))

    // Create DataFrame
    val employees = Seq(
      (1, "Alice", "Engineering", 90000),
      (2, "Bob", "Sales", 45000),
      (3, "Charlie", "Engineering", 95000),
      (4, "David", "Marketing", 55000),
      (5, "Eve", "Sales", 65000),
      (6, "Frank", "Engineering", 40000),
      (7, "Grace", "Marketing", 48000)
    ).toDF("employee_id", "name", "department", "salary")

    println("Original DataFrame:")
    employees.show()

    // Add annual_salary column (monthly salary * 12)
    val withAnnual = employees.withColumn("annual_salary", $"salary" * 12)
    println("With annual_salary column:")
    withAnnual.show()

    // Filter salary > 50,000
    val filtered = withAnnual.filter($"salary" > 50000)
    println("Filtered (salary > 50000):")
    filtered.show()

    // Group by department, calculate average salary
    val avgByDept = filtered.groupBy("department")
      .agg(avg("salary").as("avg_salary"))

    // Sort the result
    val sorted = avgByDept.orderBy(desc("avg_salary"))
    println("Average salary per department (sorted):")
    sorted.show()

    // Save as Parquet
    sorted.write.parquet(outputPath)
    println("Saved result to: " + outputPath)

    spark.stop()
  }
}
