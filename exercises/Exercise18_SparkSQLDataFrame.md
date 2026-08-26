# Exercise 18: Spark SQL and DataFrame Operations

## Code
See `src/main/scala/exercises/Exercise18_SparkSQLDataFrame.scala`

## Steps performed
1. Created a DataFrame with employee_id, name, department, salary
2. Added a derived column `annual_salary` = salary * 12 using `withColumn`
3. Filtered rows where salary > 50,000 using `filter`
4. Grouped by department and computed average salary using `groupBy` + `agg(avg(...))`
5. Sorted results by average salary descending using `orderBy(desc(...))`
6. Saved the final result as a Parquet file using `write.parquet`

## Why Parquet
Parquet is a columnar storage format - efficient for analytical queries
since it only reads the columns needed rather than entire rows, and
supports compression well. It's the standard output format for Spark SQL
pipelines that feed into other analytics tools.

## Result
| department | avg_salary |
|---|---|
| Engineering | 92500.0 |
| Sales | 65000.0 |
| Marketing | 51500.0 |
