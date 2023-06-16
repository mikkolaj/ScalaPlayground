package scala_concepts.type_classes

//import scala_concepts.type_classes.Show
import Show.ops._

object FinalExample extends App {
  // Default implicits are imported as Category 2, so it's possible to define our own implementation
  implicit val intCanShowDifferently: Show[Int] = (int: Int) => s"Int: $int"
  println(show(25))
}

trait Show[A] {
  def show(a: A): String
}

object Show {
  def apply[A: Show]: Show[A] = implicitly[Show[A]]

  object ops {
    def show[A: Show](a: A): String = Show[A].show(a)

    implicit class ShowOps[A: Show](a: A) {
      def show: String = Show[A].show(a)
    }
  }

  implicit val intCanShow: Show[Int] = (int: Int) => s"Int: $int"
  implicit val stringCanShow: Show[String] = (string: String) => s"String: $string"
}
