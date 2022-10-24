package akka_http.authentication

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{authenticateBasic, complete, path}
import akka.http.scaladsl.server.directives.Credentials

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object PasswordAuth extends App {
  // Test: curl -u mikkolaj:imAuthorized localhost:8080/magic-number

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "ProtectedSystem")
  implicit val ec: ExecutionContextExecutor = system.executionContext

  import akka_http.marshalling.Marshallers._

  def authenticator(credentials: Credentials): Option[String] = {
    credentials match {
      case p@Credentials.Provided(username) if p.verify("imAuthorized") => Some(username)
      case _ => None
    }
  }

  val route = path("magic-number") {
    authenticateBasic("Super secure system", authenticator) { username =>
      system.log.info(s"Serving user: $username")
      complete(1337)
    }
  }

  val server = Http().newServerAt("localhost", 8080).bind(route)

  StdIn.readLine()
  server.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
