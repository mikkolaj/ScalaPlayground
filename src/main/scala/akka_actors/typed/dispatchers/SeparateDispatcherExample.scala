package akka_actors.typed.dispatchers

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object SeparateDispatcherExample {
  def main(args: Array[String]): Unit = {
    // example of executing blocking calls in a separate dispatcher. PrintActors will be able to handle messages
    // while CorrectBlockingActor's dispatcher is busy handling blocking calls
    val root = Behaviors.setup[Nothing] { context =>
      for (i <- 1 to 100) {
        context.spawn(CorrectBlockingActor(), s"blockingActor-$i") ! i
        context.spawn(PrintActor(), s"printActor-$i") ! i
      }
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](root, "SeparateDispatcherExample")
  }
}
