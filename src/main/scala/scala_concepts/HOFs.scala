package scala_concepts

import scala.annotation.tailrec

// based on: https://www.youtube.com/watch?v=GG19t3xn0D0

object HOFs extends App {
  def applyNTimes(f: Int => Int, n: Int): Int => Int = (x: Int) => {
    (1 to n).foldLeft(x)((acc, _) => f(acc))
  }

  def applyNTimesRecursion(f: Int => Int, n: Int): Int => Int = (x: Int) => {
    if (n <= 0) x
    else applyNTimesRecursion(f, n - 1)(f(x))
  }

  val increment30Times: Int => Int = applyNTimes(x => x + 1, 30)
  val multiply10times: Int => Int = applyNTimes(x => x * 2, 10)

  println(increment30Times(1))
  println(multiply10times(1))

  val increment30TimesRecursion: Int => Int = applyNTimesRecursion(x => x + 1, 30)
  val multiply10TimesRecursion: Int => Int = applyNTimesRecursion(x => x * 2, 10)

  println(increment30TimesRecursion(1))
  println(multiply10TimesRecursion(1))

  def applyNTimesTailRecursion(f: Int => Int, n: Int): Int => Int = (x: Int) => {
    @tailrec
    def applyNTimesTail(f: Int => Int, n: Int, acc: Int): Int = {
      if (n <= 0) acc
      else applyNTimesTail(f, n - 1, f(acc))
    }

    applyNTimesTail(f, n, x)
  }

  val increment30TimesTailRecursion: Int => Int = applyNTimesTailRecursion(x => x + 1, 30)
  val multiply10TimesTailRecursion: Int => Int = applyNTimesTailRecursion(x => x * 2, 10)

  println(increment30TimesTailRecursion(1))
  println(multiply10TimesTailRecursion(1))
}
