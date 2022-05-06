package scala_concepts.futures

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration.Inf
import scala.concurrent._

object ExecutionContexts extends App {
  // execution context is required to execute futures in the background
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

  val futures: ListBuffer[Future[Unit]] = mutable.ListBuffer()

  for (_ <- 1 to 1000) {
    futures += Future {
      // blocking construct informs the execution context that the call is blocking,
      // consequently allowing it to use more threads
      blocking {
        println(Thread.currentThread())
        Thread.sleep(10000)
      }
    }
  }

  val futureSeq = Future.sequence(futures)

  Await.ready(futureSeq, Inf)

}
