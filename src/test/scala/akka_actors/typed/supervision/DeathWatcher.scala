package akka_actors.typed.supervision

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, TestProbe}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, Terminated}

class DeathWatcher(testKit: ActorTestKit, actor: ActorRef[_]) {
  val testProbe: TestProbe[Terminated] = testKit.createTestProbe()

  def watch(): Behavior[Terminated] = {
    Behaviors.setup { ctx =>
      ctx.watch(actor)
      Behaviors.receiveMessage {
        case e: Terminated =>
          testProbe ! e
          Behaviors.same
        case _ => Behaviors.same
      }
    }
  }

  def expectTerminated(): Unit = {
    testProbe.expectTerminated(actor)
  }

}
