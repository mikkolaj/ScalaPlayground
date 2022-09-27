package akka_actors.typed.dispatchers

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{Behavior, DispatcherSelector}

import scala.concurrent.{ExecutionContext, Future}

object CorrectBlockingActor {
  def apply(): Behavior[Int] = {
    Behaviors.setup { context =>
      // WARNING! - wrapping a blocking call in a Future and then executing it in arbitrary execution context is not
      // a solution. Executing many blocking calls in this context will block it the same way as in IncorrectBlockingActor
      // implicit val executionContext: ExecutionContext = context.executionContext

      // The solution is to execute blocking calls in a dedicated dispatcher. This technique is called "bulk-heading"
      // or "isolating blocking". By doing so we isolate blocking calls an the rest of the system is not impacted by their execution.
      // We can define properties of this dispatcher in application.conf and specify the maximum number of threads it
      // can use.
      implicit val executionContext: ExecutionContext = blockingDispatcher(context)

      Behaviors.receiveMessage { secondsToWait =>
        wrappedBlockingCall(secondsToWait)
        Behaviors.same
      }
    }
  }

  def wrappedBlockingCall(secondsToWait: Int)(implicit ec: ExecutionContext): Future[Unit] = {
    // The following two lines are going to be executed by a default dispatcher. It can create lots of Future objects
    // and cause OOM error if too many requests are accepted.
    println(s"Started blocking for $secondsToWait seconds")
    Future {
      Thread.sleep(secondsToWait * 1000)
      println(s"Finished blocking for $secondsToWait seconds")
    }
  }

  private def blockingDispatcher(ctx: ActorContext[Int]) = {
    ctx.system.dispatchers.lookup(DispatcherSelector.fromConfig("default-blocking-dispatcher"))
  }
}
