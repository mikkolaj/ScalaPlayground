package testing_objects.objects

class BookService {
  def isValidBook(name: String): Boolean = {
    BookValidator.isNiceBook(name)
  }
}
