package exercises

import org.apache.spark.sql.SparkSession

object Exercise11_DAGAndStages {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Exercise 11 - DAG and Stages")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext

    val outputPath = "data/output_wordcount"

    // Clean up previous output if it exists
    import java.io.File
    def deleteRecursively(f: File): Unit = {
      if (f.isDirectory) f.listFiles().foreach(deleteRecursively)
      f.delete()
    }
    deleteRecursively(new File(outputPath))

    // textFile -> flatMap -> filter -> map -> reduceByKey -> saveAsTextFile
    val lines = sc.textFile("data/sample.txt")               // input
    val words = lines.flatMap(_.split(" "))                   // narrow
    val filtered = words.filter(_.length > 2)                 // narrow
    val pairs = filtered.map(word => (word.toLowerCase, 1))   // narrow
    val counts = pairs.reduceByKey(_ + _)                      // wide (shuffle)

    counts.saveAsTextFile(outputPath)                          // action

    println("Job complete. Output written to: " + outputPath)
    println("\nDAG / lineage:")
    println(counts.toDebugString)

    spark.stop()
  }
}
