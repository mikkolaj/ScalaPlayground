package testing_objects.OOP_Simple

class BookService(bookValidator: BookValidator = BookValidator.defaultBookValidator) {
  def isValidBook(name: String): Boolean = {
    bookValidator.isNiceBook(name)
  }
}
