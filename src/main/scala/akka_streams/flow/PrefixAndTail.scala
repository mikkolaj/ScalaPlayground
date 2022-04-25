package akka_streams.flow

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.Source
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext
import scala.util.Failure

object PrefixAndTail extends App {
  implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "TestSystem")
  implicit val executionContext: ExecutionContext = actorSystem.executionContext
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  val ExampleDataIterator = List("TITLE", "DATA1", "DATA2").iterator
  val source = Source.fromIterator(() => ExampleDataIterator)

  //  val aggregationResult = Source.empty // throws exception
  val aggregationResult = source // completes successfully
    // prefixAndTail(n) splits the stream into:
    // 1) head: a sequence containing up to n leading elements
    // 2) tail: the rest of the stream
    // head might contain less elements than n if the upstream
    // does not provide enough elements
    .prefixAndTail(1)
    .map {
      case (Nil, _) => throw new RuntimeException("Stream does not contain required metadata.")
      case (head, tail) => new Aggregator(head.head, tail)
    }
    .flatMapConcat(_.aggregate())
    .runForeach(println)

  aggregationResult
    .onComplete { result =>
      result match {
        case Failure(exception) => logger.error(exception.getMessage)
        case _ =>
      }
      actorSystem.terminate()
    }
}
