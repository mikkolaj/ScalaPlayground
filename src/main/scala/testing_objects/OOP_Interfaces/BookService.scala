package testing_objects.OOP_Interfaces

class BookService(bookValidator: BookValidator = BookValidator.defaultBookValidator) {
  def isValidBook(name: String): Boolean = {
    bookValidator.isNiceBook(name)
  }
}
