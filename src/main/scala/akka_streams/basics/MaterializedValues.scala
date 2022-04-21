package akka_streams.basics

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink}
import akka_streams.datastructures.{Tweet, TweetResource}

import scala.concurrent.{ExecutionContext, Future}

object MaterializedValues extends App {
  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val executionContext: ExecutionContext = system.dispatcher

  val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ => 1)

  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  val counterGraph: RunnableGraph[Future[Int]] =
    TweetResource.tweets.via(count).toMat(sumSink)(Keep.right)

  // materialization happens only after calling run, RunnableGraph
  // is a blueprint that might result in different values after each run
  // (based on the source)
  val sum: Future[Int] = counterGraph.run()

  // the above code is equivalent to the following one liner:
  val sum2: Future[Int] = TweetResource.tweets.map(_ => 1).runWith(sumSink)

  sum.foreach(c => println(s"Total tweets processed: $c"))
  sum.onComplete(_ => system.terminate())
}
