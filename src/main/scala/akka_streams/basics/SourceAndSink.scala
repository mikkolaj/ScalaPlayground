package akka_streams.basics

// based on: https://doc.akka.io/docs/akka/current/stream/stream-quickstart.html

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import java.nio.file.Paths
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

object SourceAndSink extends App {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val executionContext: ExecutionContext = system.dispatcher

  // source is parametrized with two values
  // 1) value produced by the source
  // 2) materialized value - some additional value, e.g. a network
  //    source might want to provide information about the bound port
  // when materialized value is not produced, NotUsed type is provided
  val source: Source[Int, NotUsed] = Source(1 to 100)

  // only after running the source will it activate
  // runForeach returns Future[Done] indicating end of processing
  val done: Future[Done] = source.runForeach(i => println(i))

  // source, flow, sink (basically all composing elements) are reusable
  // scan is similiar to fold but emits every intermediate result
  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  def fileWritingSink(fileName: String): Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get(fileName))

  val result: Future[IOResult] = factorials.map(num => ByteString(s"$num\n")).runWith(fileWritingSink("factorials.txt"))

  // flow is like a source, but with "open" input
  val lineSink: Sink[String, Future[IOResult]] =
  // we convert each flowing string into a ByteString
    Flow[String].map(s => ByteString(s + "\n"))
      // toMat connects a flow and sink, Keep.right is a function
      // wchich combines materialized values (from flow and sink) into one
      // by taking the rightmost one (from sink)
      .toMat(fileWritingSink("factorials2.txt"))(Keep.right)

  val resultWithSinkFromFlow = factorials.map(_.toString).runWith(lineSink)

  val slowResult = factorials
    .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
    // slows the speed at which elements are sent downstream and produced
    // from upstream (back-pressure mechanism)
    .throttle(1, 1.second)
    .runForeach(println)

  // akka streams implicitly implements flow control, all operators respect back-pressure

  val allResults = List(done, result, resultWithSinkFromFlow, slowResult)
  // to exit we need to terminate the ActorSystem, we do it after all processing is done
  Future.sequence(allResults).onComplete(_ => system.terminate())
}
