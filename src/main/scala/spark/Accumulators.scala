package spark

import org.apache.spark.util.LongAccumulator
import spark.Utils.updateCounts

import scala.io.StdIn
import scala.math.pow

object Accumulators extends App {
  val sc = SparkInitializer.sc

  val evenNumbers = sc.longAccumulator("Even numbers")
  val oddNumbers = sc.longAccumulator("Odd numbers")
  val data = sc.parallelize(1 to 1000)

  // updates in transformations are not guaranteed to be computed exactly once
  // in case of task/stage failure multiple updates might be performed
  val result = data.map { x =>
    updateCounts(x, evenNumbers, oddNumbers)
    pow(x, 2)
  }.collect()

  println(result(999))
  // only the driver can read value of the accumulator
  println(evenNumbers.value)
  println(oddNumbers.value)

  // updates in actions have a guarantee of executing exactly once
  // accumulators can be used across many jobs
  data.foreach(x => updateCounts(x, evenNumbers, oddNumbers))

  println(evenNumbers.value)
  println(oddNumbers.value)

  StdIn.readLine()
}

object Utils {
  def updateCounts(number: Int, evenNumbers: LongAccumulator, oddNumbers: LongAccumulator): Unit = {
    if (number % 2 == 0) {
      evenNumbers.add(1)
    } else {
      oddNumbers.add(1)
    }
  }
}
