package akka_streams.junctions

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ClosedShape, UniformFanOutShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink}
import akka_streams.datastructures.{Author, Hashtag, Tweet, TweetResource}

import scala.concurrent.{ExecutionContext, Future}

object BroadcastExample extends App {
  implicit val system: ActorSystem = ActorSystem("reactive-tweets")
  implicit val executionContext: ExecutionContext = system.dispatcher

  // junctions are structures that perform fan-out or fan-in distribution of elements
  // from a stream, example of such construct is Broadcast

  // stream structures formed using junctions are called Graphs and are constructed using GraphDSL

  val writeAuthors: Sink[Author, Future[Done]] = Sink.foreach((author: Author) => println(s"Author: ${author.handle}"))
  val writeHashtags: Sink[Hashtag, Future[Done]] = Sink.foreach((hashtag: Hashtag) => println(s"Hashtag: ${hashtag.name}"))

  // fromGraph is able to transform a closed graph into a runnable graph
  // Graph and RunnableGraph are immutable, thread-safe and shareable
  val graph: RunnableGraph[NotUsed] = RunnableGraph.fromGraph(GraphDSL.create() { implicit graphBuilder =>
    import GraphDSL.Implicits._

    val bcast: UniformFanOutShape[Tweet, Tweet] = graphBuilder.add(Broadcast[Tweet](2))
    TweetResource.tweets ~> bcast.in
    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashtags

    // closed shape means it's a fully connected ("closed") graph
    ClosedShape
    // there are other graph shapes with one or more unconnected ports,
    // such a graph is called a partial graph and can be used to compose and
    // nest graphs or even form sources, sinks and flows
  })

  graph.run()
}
