package spark


object BroadcastVars extends App {
  val sc = SparkInitializer.sc
  val bigList = List.from(1 to 1000)

  // ships a variable to every node in the cluster, useful when we want to cache some value
  val broadcastVar = sc.broadcast(bigList)

  // instead of using bigList use broadcastVar to avoid sending it in each task
  val result = sc.parallelize(1 to 1000).map { number =>
    val rest = broadcastVar.value.filter(_ != number)
    (number, rest)
  }.collect()

  println(s"Result: ${result(0)}")

  // destroy after use
  broadcastVar.destroy()
}
