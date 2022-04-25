package akka_streams.flow

import akka.NotUsed
import akka.stream.scaladsl.Source

import scala.collection.{mutable => m}
import scala.concurrent.ExecutionContext

class Aggregator(head: String, data: Source[String, NotUsed])(implicit executionContext: ExecutionContext) {
  def aggregate(): Source[String, NotUsed] = {
    val title = s"My head is: $head\n"
    data.fold(new m.StringBuilder(title))((builder, elem) => builder.append(elem + "\n")).map(builder => builder.toString())
  }
}
