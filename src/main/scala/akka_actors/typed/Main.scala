package akka_actors.typed

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka_actors.typed.messages.SayHi

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem[Nothing](Behaviors.empty, "ShoppingCartService")
    val classActor = new ActorFromClass(3)

    val classActorRef = system.systemActorOf(classActor(0), "classActor")
    val objectActorRef = system.systemActorOf(ActorFromObject(0, 3), "objectActor")

    LazyList.from(0).takeWhile(_ < 4).foreach(_ => {
      classActorRef ! SayHi("classActor", system.ignoreRef)
      objectActorRef ! SayHi("objectActor", system.ignoreRef)
    })
  }
}
