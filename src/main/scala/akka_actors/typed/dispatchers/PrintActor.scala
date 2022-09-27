package akka_actors.typed.dispatchers

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object PrintActor {
  def apply(): Behavior[Int] = {
    Behaviors.receiveMessage { number =>
      println(s"PrintActor received: $number")
      Behaviors.same
    }
  }
}
