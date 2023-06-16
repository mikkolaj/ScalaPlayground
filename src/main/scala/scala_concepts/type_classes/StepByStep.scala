package scala_concepts.type_classes

import scala_concepts.type_classes.Show.{ShowOps, intCanShow, show}

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

trait Show[A] {
  def show(a: A): String
}


object Show {
  def show[A](a: A)(implicit sh: Show[A]): String = sh.show(a)

  // syntax sugar equivalent to the above (implicit Show[A] is present in the scope of the method)
  // relationship A: B (context bound) can be interpreted as A 'has a' B
  def show2[A: Show](a: A): String = implicitly[Show[A]].show(a)

  // apply function can shorten the show2 function
  def apply[A](implicit sh: Show[A]): Show[A] = sh

  def show3[A: Show](a: A): String = Show[A].show(a)

  implicit val intCanShow: Show[Int] = (int: Int) => s"Int: $int"

  // implicit Ops class enables calling show like a method
  implicit class ShowOps[A: Show](a: A) {
    def show: String = Show[A].show(a)
  }
}
