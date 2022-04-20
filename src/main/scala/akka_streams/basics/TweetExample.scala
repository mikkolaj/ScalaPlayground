package akka_streams.basics

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka_streams.datastructures.{Author, Hashtag, Tweet, TweetResource}

import scala.concurrent.ExecutionContext

object TweetExample {
  implicit val system: ActorSystem = ActorSystem("reactive-tweets")
  implicit val executionContext: ExecutionContext = system.dispatcher

  def main(args: Array[String]): Unit = {
    TweetResource.tweets
      .filterNot(_.hashtags.contains(TweetResource.akkaTag)) // Remove all tweets containing #akka hashtag
      .map(_.hashtags) // Get all sets of hashtags ...
      .reduce(_ ++ _) // ... and reduce them to a single set, removing duplicates across all tweets
      .mapConcat(identity) // Flatten the set of hashtags to a stream of hashtags
      .map(_.name.toUpperCase) // Convert all hashtags to upper case
      .runWith(Sink.foreach(println)) // Attach the Flow to a Sink that will finally print the hashtags
      .onComplete(_ => system.terminate())
  }
}
