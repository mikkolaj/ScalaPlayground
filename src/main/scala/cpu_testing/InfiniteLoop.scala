package cpu_testing

import scala.collection.mutable

object InfiniteLoop extends App {
  val list = mutable.ListBuffer[BigInt]()
  LazyList.from(1).foreach { number =>
    if (number % 2 == 1) {
      list += number * number
    } else {
      list.headOption.foreach(el => list -= el)
    }
  }
}
