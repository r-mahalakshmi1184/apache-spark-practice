package exercises

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.broadcast

object Exercise19_JoinsAndBroadcastJoin {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 19 - DataFrame Joins and Broadcast Join")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Employees: employee_id, name, department_id
    val employees = Seq(
      (1, "Alice", 101),
      (2, "Bob", 102),
      (3, "Charlie", 101),
      (4, "David", 103),
      (5, "Eve", 999)  // no matching department - to demonstrate left join
    ).toDF("employee_id", "name", "department_id")

    // Departments: department_id, department_name
    val departments = Seq(
      (101, "Engineering"),
      (102, "Sales"),
      (103, "Marketing")
    ).toDF("department_id", "department_name")

    println("Employees:")
    employees.show()

    println("Departments:")
    departments.show()

    // Inner join - only matching rows on both sides
    val innerJoin = employees.join(departments, Seq("department_id"), "inner")
    println("Inner join result:")
    innerJoin.show()

    // Left join - all employees, department_name is null if no match
    val leftJoin = employees.join(departments, Seq("department_id"), "left")
    println("Left join result (Eve has no department match):")
    leftJoin.show()

    // Broadcast join - explicitly hint Spark to broadcast the small Departments table
    val broadcastJoin = employees.join(broadcast(departments), Seq("department_id"), "inner")
    println("Broadcast join result:")
    broadcastJoin.show()

    println("Broadcast join physical plan (look for BroadcastHashJoin):")
    broadcastJoin.explain()

    spark.stop()
  }
}
