package akka_streams.basics

import akka.actor.ActorSystem
import akka.stream.OverflowStrategy
import akka_streams.datastructures.TweetResource

import scala.concurrent.ExecutionContext

object Buffering extends App {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val executionContext: ExecutionContext = system.dispatcher

  // buffering allows to queue up a number of elements for later processing,
  // when our processing function is slower than the producer and it is behind an async boundary
  // OverflowStrategy defines how to deal with elements that don't fit in
  // the buffer anymore
  val done = TweetResource.tweets
    .buffer(3, OverflowStrategy.dropHead)
    .async
    .runForeach(delayedPrint)

  done.onComplete(_ => system.terminate())

  def delayedPrint(x: Any): Unit = {
    Thread.sleep(1000)
    println(x)
  }
}
