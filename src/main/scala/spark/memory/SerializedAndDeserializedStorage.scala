package spark.memory

import org.apache.spark.storage.StorageLevel
import spark.SparkInitializer

import scala.io.StdIn

object SerializedAndDeserializedStorage extends App {

  val sc = SparkInitializer.sc
  val data = sc.parallelize(1 to 500000000)
  val transformed = data.map(x => x+1)

  // it's possible to determine size of an RDD by looking it up on SparkUI storage tab
  transformed.persist()
  transformed.count()

  // Deserialized size: 1907.3 MiB
  StdIn.readLine()

  transformed.unpersist()
  transformed.persist(StorageLevel.MEMORY_ONLY_SER)
  transformed.count()

  // Serialized size: 2.7 GiB!
  StdIn.readLine()
}
