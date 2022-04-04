package testing_objects.OOP_Interfaces

object Main extends App {
  val bookService = new BookService
  println(bookService.isValidBook("Hobbit"))
}
