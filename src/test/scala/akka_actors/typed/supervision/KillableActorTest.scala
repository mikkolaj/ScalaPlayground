package akka_actors.typed.supervision

import akka.actor.DeadLetter
import akka.actor.testkit.typed.scaladsl.{LogCapturing, ScalaTestWithActorTestKit}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, SupervisorStrategy}
import akka_actors.typed.supervision.KillableActor.{GetState, Throw, UpdateState}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.featurespec.AnyFeatureSpecLike

class KillableActorTest extends ScalaTestWithActorTestKit with AnyFeatureSpecLike with LogCapturing with BeforeAndAfterEach {
  var actor: ActorRef[KillableActor.Command] = _

  override def afterEach(): Unit = {
    testKit.stop(actor)
  }

  Feature("Killable Actor") {
    val actorName = "KillableActor"
    val actorInitialBehavior = KillableActor(0)
    val throwRuntimeException = Throw(new RuntimeException("Test"))

    Scenario("Actor correctly updates state") {
      actor = testKit.spawn(actorInitialBehavior, actorName)
      val probe = testKit.createTestProbe[Int]()

      actor ! GetState(probe.ref)
      probe.expectMessage(0)
      actor ! UpdateState(42)
      actor ! GetState(probe.ref)
      probe.expectMessage(42)
    }

    Scenario("Default supervision, no restarts, actor stopped") {
      actor = testKit.spawn(actorInitialBehavior, actorName)
      val deadLetter = testKit.createDeadLetterProbe()
      val watcher = new DeathWatcher(testKit, actor)
      watcher.watch()

      actor ! throwRuntimeException
      actor ! throwRuntimeException

      watcher.expectTerminated()
      deadLetter.expectMessageType[DeadLetter]
    }

    Scenario("Restart supervision, actor recovers from failure") {
      val supervisedBehavior = Behaviors.supervise(actorInitialBehavior).onFailure[RuntimeException](SupervisorStrategy.restart)
      actor = testKit.spawn(supervisedBehavior, actorName)
      val probe = testKit.createTestProbe[Int]()

      actor ! UpdateState(42)

      actor ! throwRuntimeException

      actor ! GetState(probe.ref)
      probe.expectMessage(0)
    }


    Scenario("Complex supervision, actor resumes if failure can be ignored, restarts to recover from failure and stops on fatal exceptions") {
      val resumeOnRuntimeException = Behaviors.supervise(actorInitialBehavior).onFailure[RuntimeException](SupervisorStrategy.resume)
      val restartOnException = Behaviors.supervise(resumeOnRuntimeException).onFailure[Exception](SupervisorStrategy.restart)
      val stopOnError = Behaviors.supervise(restartOnException).onFailure[Error](SupervisorStrategy.stop)

      actor = testKit.spawn(stopOnError, actorName)
      val probe = testKit.createTestProbe[Int]()
      val watcher = new DeathWatcher(testKit, actor)
      watcher.watch()

      actor ! UpdateState(42)
      actor ! throwRuntimeException

      actor ! GetState(probe.ref)
      probe.expectMessage(42)

      actor ! Throw(new Exception("Serious exception"))
      actor ! GetState(probe.ref)
      probe.expectMessage(0)

      actor ! Throw(new Error("An Error!"))
      watcher.expectTerminated()
    }
  }

}
