package testing_objects.objects.injection

import testing_objects.objects.BookValidator

class BookService(validator: IBookValidator = BookValidator) {
  def isValidBook(name: String): Boolean = {
    validator.isNiceBook(name)
  }
}
