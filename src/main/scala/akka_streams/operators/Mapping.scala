package akka_streams.operators

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.{Flow, Keep, Source}

import scala.concurrent.{ExecutionContext, Future}

class Mapping {
  private val twoElementListMapper = (elem: Int) => List(s"$elem", s"$elem-snd")

  def toAlteredString(x: Int): String = {
    (x + 1).toString
  }

  def joinStrings(a: String, b: String): String = a + b + " "

  def toListOfStrings(elem: Int): Seq[String] = twoElementListMapper(elem)

  def statefulToListOfStrings(): Int => List[String] = {
    var power = 1L

    (element: Int) => {
      power *= 2
      List(element, power).map(_.toString)
    }
  }

  def toSourceOfStrings(elem: Int): Source[String, NotUsed] = {
    Source.fromIterator(() => twoElementListMapper(elem).iterator)
  }

  def toFlow(head: Seq[Int]): Flow[Int, String, NotUsed] = {
    Flow[Int].map(elem => (elem + head.head).toString)
  }
}

object Mapping extends App {
  implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "TestSystem")
  implicit val executionContext: ExecutionContext = actorSystem.executionContext

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val EmptyString = ""
  val mapping = new Mapping

  import mapping._

  val mapResult = source
    // map transforms each element into another element of arbitrary type
    .map(toAlteredString)
    .runFold(EmptyString)(joinStrings)

  mapResult.foreach(result => println(s"map result: $result"))

  val mapConcatResult = source
    // mapConcat transforms each element into a sequence of elements of arbitrary type
    // and concatenates those sequences into one
    .mapConcat(toListOfStrings)
    .runFold(EmptyString)(joinStrings)

  mapConcatResult.foreach(result => println(s"mapConcat result: $result"))

  val statefulMapConcatResult = source
    // statefulMapConcat is like mapConcat but allows for storing state
    .statefulMapConcat(statefulToListOfStrings)
    .runFold(EmptyString)(joinStrings)

  statefulMapConcatResult.foreach(result => println(s"statefulMapConcat result: $result"))

  val flatMapConcatResult = source
    // flatMapConcat transforms each element into a source of elements of arbitrary type
    // and concatenates those sources into one, then makes this source the one to process
    // in next steps
    .flatMapConcat(toSourceOfStrings)
    .runFold(EmptyString)(joinStrings)

  flatMapConcatResult.foreach(result => println(s"flatMapConcat result: $result"))

  val flatMapPrefixResult = source
    // flatMapPrefix(n)(func) takes up to n elements from the source and maps func on them,
    // resulting in a Flow, this Flow is then applied to the rest of the stream
    .flatMapPrefix(1)(toFlow)
    .runFold(EmptyString)(joinStrings)

  flatMapPrefixResult.foreach(result => println(s"flatMapPrefix result: $result"))

  val flatMapMerge = source
    // flatMapMerge(breadth, func) transforms each element into a source of elements of arbitrary type
    // and then merges those sources into the output stream, breadth specifies the maximum number of sources
    // that can be active at a given time
    // events from different sources can interleave in any order but events from the same source will
    // preserve the order provided by the source
    // e.g. flatMapMerge(3, toSourceOfStrings)
    // flatMapMerge result: 3 2 1 3-snd 2-snd 1-snd 6 5 4 6-snd 5-snd 4-snd 9 8 7 9-snd 8-snd 7-snd 10 10-snd
    // toSourceOfStrings returns a source in the form of Source("elem", "elem-snd")
    // the order of elements emitted from this source is preserved - "elem-snd"
    // is always after "elem"
    .flatMapMerge(3, toSourceOfStrings)
    .runFold(EmptyString)(joinStrings)

  flatMapMerge.foreach(result => println(s"flatMapMerge result: $result"))

  val allResults = List(mapResult, mapConcatResult, statefulMapConcatResult, flatMapConcatResult, flatMapPrefixResult)
  Future.sequence(allResults).onComplete(_ => actorSystem.terminate())
}
