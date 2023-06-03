package cpu_testing

import scala.collection.mutable

object InfiniteLoop extends App {
  val list = mutable.ListBuffer()
  LazyList.from(1).foreach { number =>
    if (number * number % 2 == 0) {
      list += number * number
    } else {
      list -= list.head
    }
  }
}
