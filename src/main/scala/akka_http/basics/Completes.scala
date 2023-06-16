package akka_http.basics

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, concat, path}
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object Completes {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "DifferentCompleteCalls")
  implicit val ec: ExecutionContextExecutor = system.executionContext

  val routes: Route = concat(
    path("a") {
      complete("foo")
    },
    path("b") {
      complete(StatusCodes.OK)
    },
    path("c") {
      complete(StatusCodes.Created -> "bar")
    },
    path("d") {
      complete(201 -> "bar")
    },
    path("e") {
      complete(StatusCodes.Created, List(`Content-Type`(`text/plain(UTF-8)`)), "bar")
    },
    path("f") {
      complete(Future {
        StatusCodes.Created -> "bar"
      })
    },
    path("g") {
      complete(201, List(`Content-Type`(`text/plain(UTF-8)`)), Future {
        "bar"
      })
    },
    path("h") {
      complete(HttpResponse(status = OK, entity = "foo"))
    },
    path("i") & complete("baz") // `&` also works with `complete` as the 2nd argument
  )

  def main(args: Array[String]): Unit = {
    val server = Http().newServerAt("localhost", 8080).bind(routes)

    StdIn.readLine()

    server.foreach { binding =>
      binding.unbind()
      system.terminate()
    }
  }
}
