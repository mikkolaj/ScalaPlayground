package testing_objects.objects

object Main extends App {
  val bookService = new BookService
  println(bookService.isValidBook("Hobbit"))
}
