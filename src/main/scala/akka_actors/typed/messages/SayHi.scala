package akka_actors.typed.messages

import akka.actor.typed.ActorRef

final case class SayHi(name: String, replyTo: ActorRef[Hi])
