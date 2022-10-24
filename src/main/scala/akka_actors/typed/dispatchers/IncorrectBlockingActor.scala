package akka_actors.typed.dispatchers

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object IncorrectBlockingActor {
  def apply(): Behavior[Int] = {
    Behaviors.receiveMessage { secondsToWait =>
      // Blocking inside an actor that uses default dispatcher is an example of incorrect code
      // when all available threads are blocked, then all other actors will be starved
      Thread.sleep(secondsToWait * 1000)
      println(s"Finished blocking for $secondsToWait seconds")
      Behaviors.same
    }
  }
}
