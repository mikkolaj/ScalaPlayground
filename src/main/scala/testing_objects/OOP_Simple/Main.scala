package testing_objects.OOP_Simple

object Main extends App {
  val bookService = new BookService
  println(bookService.isValidBook("Hobbit"))
}
