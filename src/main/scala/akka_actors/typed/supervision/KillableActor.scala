package akka_actors.typed.supervision

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object KillableActor {
  sealed trait Command

  case class UpdateState(number: Int) extends Command

  case class GetState(replyTo: ActorRef[Int]) extends Command

  case class Throw(throwable: Throwable) extends Command

  def apply(number: Int): Behavior[Command] = Behaviors.receiveMessage {
    case UpdateState(number) => apply(number)
    case GetState(replyTo) =>
      replyTo ! number
      Behaviors.same
    case Throw(throwable) => throw throwable
  }

}
