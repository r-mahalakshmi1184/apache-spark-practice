package exercises

import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

object Exercise16_CacheVsPersist {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 16 - Cache vs Persist")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    val data = sc.parallelize(1 to 1000000)

    // --- Without caching ---
    val expensive1 = data.map(x => x * x)
    val start1 = System.currentTimeMillis()
    val sum1 = expensive1.sum()
    val count1 = expensive1.count()
    val time1 = System.currentTimeMillis() - start1
    println(s"Without caching -> sum=$sum1, count=$count1, time=${time1}ms")
    println("(expensive transformation recomputed for EACH action)")

    // --- With cache() ---
    val expensive2 = data.map(x => x * x).cache()
    val start2 = System.currentTimeMillis()
    val sum2 = expensive2.sum()
    val count2 = expensive2.count()
    val time2 = System.currentTimeMillis() - start2
    println(s"\nWith cache() -> sum=$sum2, count=$count2, time=${time2}ms")
    println("(computed once on first action, reused on second)")
    expensive2.unpersist()

    // --- With persist() using a different storage level ---
    val expensive3 = data.map(x => x * x).persist(StorageLevel.MEMORY_AND_DISK)
    val start3 = System.currentTimeMillis()
    val sum3 = expensive3.sum()
    val count3 = expensive3.count()
    val time3 = System.currentTimeMillis() - start3
    println(s"\nWith persist(MEMORY_AND_DISK) -> sum=$sum3, count=$count3, time=${time3}ms")
    expensive3.unpersist()

    spark.stop()
  }
}
