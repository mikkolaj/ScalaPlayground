package akka_actors.typed

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka_actors.typed.messages.{Hi, SayHi}

object ActorFromObject {
  def apply(requestsServed: Int, maxRequests: Int): Behavior[SayHi] = Behaviors.receive { (context, message) =>
    context.log.info("Hi {}!", message)
    message.replyTo ! Hi()
    if (requestsServed + 1 < maxRequests) {
      apply(requestsServed + 1, maxRequests)
    } else {
      empty()
    }
  }

  def empty(): Behavior[SayHi] = Behaviors.empty
}
