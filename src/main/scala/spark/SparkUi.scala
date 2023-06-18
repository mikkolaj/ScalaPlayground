package spark

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
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
  val session: SparkSession = SparkSession.builder()
    .master("local")
    .config("spark.executor.memory", "2G")
    .config("spark.executor.cores", "4")
    .getOrCreate()
  val sc: SparkContext = session.sparkContext
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