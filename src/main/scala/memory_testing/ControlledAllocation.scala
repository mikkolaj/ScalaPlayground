package memory_testing

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object ControlledAllocation extends App {
  val buffer: ListBuffer[Int] = ListBuffer()
  val builder = new mutable.StringBuilder

  println("Type in numbers of elements to allocate.")
  println("Press enter to exit.")

  LazyList
    .continually(readInput)
    .takeWhile(!_.isBlank)
    .map(_.toInt)
    .foreach(allocateNumbers)

  def readInput: String = scala.io.StdIn.readLine()

  def allocateNumbers(n: Int): Unit = {
    buffer.addAll(1 to n)
    println(s"Allocated $n numbers")
  }
}
