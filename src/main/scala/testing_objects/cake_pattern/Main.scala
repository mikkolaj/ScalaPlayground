package testing_objects.cake_pattern

object Main extends App {
  val bookService = new BookService
  println(bookService.isValidBook("Hobbit"))
}
