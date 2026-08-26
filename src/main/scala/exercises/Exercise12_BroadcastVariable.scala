package exercises

import org.apache.spark.sql.SparkSession

object Exercise12_BroadcastVariable {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 12 - Broadcast Variable")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    // Small lookup table: department code -> department name
    val departmentLookup = Map(
      101 -> "IT",
      102 -> "HR",
      103 -> "Finance"
    )

    // Broadcast the small lookup table to all executors
    val broadcastLookup = sc.broadcast(departmentLookup)

    // "Large" employee dataset: (employee_id, name, dept_code)
    val employees = sc.parallelize(Seq(
      (1, "Alice", 101),
      (2, "Bob", 102),
      (3, "Charlie", 101),
      (4, "David", 103),
      (5, "Eve", 102)
    ))

    // Enrich employee records using the broadcast variable
    val enriched = employees.map { case (id, name, deptCode) =>
      val deptName = broadcastLookup.value.getOrElse(deptCode, "Unknown")
      (id, name, deptName)
    }

    println("Enriched employee records:")
    enriched.collect().foreach { case (id, name, dept) =>
      println(s"ID=$id, Name=$name, Department=$dept")
    }

    spark.stop()
  }
}
