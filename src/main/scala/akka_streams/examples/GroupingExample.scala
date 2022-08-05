package akka_streams.examples

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import akka_streams.datastructures.{LineProvider, LineRecord}

import scala.concurrent.ExecutionContext

object GroupingExample extends App {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val executionContext: ExecutionContext = system.dispatcher

  val source = Source.fromIterator(() => LineProvider.lines.iterator)

  val batchSize = 4

  val result = source
    .map(LineRecord.fromLine)
    .grouped(batchSize)
    .map(batch =>
      batch
        .groupBy(_.key)
        .map(sumRecords)
    )
    .runForeach(println)

  result.onComplete(_ => system.terminate())

  private def sumRecords(mapping: (Int, Seq[LineRecord])) = mapping match {
    case (key, records) => (key, records.foldLeft(0)(_ + _.value))
  }
}
