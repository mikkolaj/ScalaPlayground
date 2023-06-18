package spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import spark.TimedResult.timedExecution

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.io.StdIn

object SparkUi extends App {
  val sc = SparkInitializer.sc
  val repartitioned = sc.parallelize(1 to 100).repartition(10)
  repartitioned.persist()
  val firstSum = timedExecution(repartitioned.sum())
  val secondSum = timedExecution(repartitioned.sum())
  println(s"First sum: $firstSum")
  println(s"Second sum: $secondSum")
  StdIn.readLine()
}

object SparkInitializer {
  def sessionBuilder(conf: SparkConf = new SparkConf): SparkSession = SparkSession.builder()
    .config(conf
      .setMaster("local")
      .set("spark.executor.memory", "2G")
      .set("spark.executor.cores", "4")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    )
    .getOrCreate()

  lazy val session: SparkSession = sessionBuilder()
  lazy val sc: SparkContext = session.sparkContext
}

case class TimedResult[T](result: T, duration: Duration)

object TimedResult extends App {
  def timedExecution[T](func: => T): TimedResult[T] = {
    val start = System.currentTimeMillis()
    val result = func
    val end = System.currentTimeMillis()
    TimedResult(result, Duration.apply(end - start, TimeUnit.MILLISECONDS))
  }
}