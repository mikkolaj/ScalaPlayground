package testing_objects.cake_pattern

class BookService(bookValidator: BookValidator = BookValidator.defaultValidator) {
  def isValidBook(name: String): Boolean = {
    bookValidator.isNiceBook(name)
  }
}
