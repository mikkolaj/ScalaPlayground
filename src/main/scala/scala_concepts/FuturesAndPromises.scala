package scala_concepts

import scala.concurrent.{ExecutionContext, Future, blocking}

object FuturesAndPromises extends App {
  implicit val ec = ExecutionContext.global

  for (i <- 1 to 33000) {
    Future {
      blocking {
        Thread.sleep(999999)
        println("FINISHED")
      }
    }
  }

}
