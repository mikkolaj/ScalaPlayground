package akka_actors.typed

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka_actors.typed.messages.{Hi, SayHi}

class ActorFromClass(val max: Int) {
  def apply(requestsServed: Int): Behavior[SayHi] = Behaviors.receive { (context, message) =>
    context.log.info("Hi {}!", message)
    message.replyTo ! Hi()
    if (requestsServed + 1 < max) {
      apply(requestsServed + 1)
    } else {
      empty()
    }
  }

  def empty(): Behavior[SayHi] = Behaviors.empty
}
