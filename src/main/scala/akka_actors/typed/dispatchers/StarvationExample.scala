package akka_actors.typed.dispatchers

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object StarvationExample {
  def main(args: Array[String]): Unit = {
    // example of actors being starved by incorrect handling of blocking calls
    // at some point print actors won't be able to process messages,
    // because all the threads are going to be taken by IncorrectBlockingActors
    // starvation will last until a thread is freed
    val root = Behaviors.setup[Nothing] { context =>
      for (i <- 1 to 100) {
        context.spawn(IncorrectBlockingActor(), s"blockingActor-$i") ! i
        context.spawn(PrintActor(), s"printActor-$i") ! i
      }
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](root, "StarvationExample")
  }
}
