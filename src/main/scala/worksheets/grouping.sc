var list = List(("anc", 1), ("ahg", 2), ("tyh", 4), ("blafdsa", 4))

val grouped = list.groupBy(x => x._1.length())

grouped.foreach(println)

println(Seq() == Nil)
