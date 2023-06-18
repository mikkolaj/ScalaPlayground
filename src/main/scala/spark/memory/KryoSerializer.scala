package spark.memory

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import spark.SparkInitializer.sessionBuilder

import scala.io.StdIn

object KryoSerializer extends App {
  val serializerConf = new SparkConf()
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .set("spark.kryo.registrationRequired", "true")

  val session = sessionBuilder(serializerConf)
  val sc = session.sparkContext

  val data = sc.parallelize(1 to 500000000)
  val transformed = data.map(x => x + 1)

  transformed.persist(StorageLevel.MEMORY_ONLY_SER)
  transformed.count()

  // Serialized size: 2.7 GiB! where are the gains? :D
  StdIn.readLine()
}
