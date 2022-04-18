package akka_http.basics

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}

import scala.concurrent.Future
import scala.concurrent.duration._

// Based on: https://blog.rockthejvm.com/a-5-minute-akka-http-client/

object RequestSender extends App {
  implicit val system: ActorSystem = ActorSystem() // akkaActors

  import system.dispatcher // "thread pool", used to serve http requests

  // akkaHttp uses akkaStreams

  val request = HttpRequest(
    method = HttpMethods.GET,
    uri = "https://pokeapi.co/api/v2/pokemon/ditto/"
  )

  def sendRequest(httpRequest: HttpRequest): Future[String] = {
    val responseFuture = Http().singleRequest(httpRequest)
    val entityFuture = responseFuture.flatMap(_.entity.toStrict(2.seconds))
    entityFuture.map(_.data.utf8String)
  }

  sendRequest(request).foreach(println)
}
