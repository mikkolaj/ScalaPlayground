package scala_concepts.type_classes

import scala_concepts.type_classes.ShowT.{ShowTOps, intCanShow, show}

// https://scalac.io/blog/typeclasses-in-scala/
object StepByStep extends App {
  // Most basic usage
  println(intCanShow.show(24))

  // Method in companion object of type class with explicit passing of showing implementation
  println(show(25)(intCanShow))

  // Implicit passing of showing implementation
  println(show(25))

  // Method like show
  println(25.show)
}

trait ShowT[A] {
  def show(a: A): String
}


object ShowT {
  def show[A](a: A)(implicit sh: ShowT[A]): String = sh.show(a)

  // syntax sugar equivalent to the above (implicit ShowT[A] is present in the scope of the method)
  // relationship A: B (context bound) can be interpreted as A 'has a' B
  def show2[A: ShowT](a: A): String = implicitly[ShowT[A]].show(a)

  // apply function can shorten the show2 function
  def apply[A](implicit sh: ShowT[A]): ShowT[A] = sh

  def show3[A: ShowT](a: A): String = ShowT[A].show(a)

  implicit val intCanShow: ShowT[Int] = (int: Int) => s"Int: $int"

  // implicit Ops class enables calling show like a method
  implicit class ShowTOps[A: ShowT](a: A) {
    def show: String = ShowT[A].show(a)
  }
}
