package testing_objects.objects.injection

class BookService(validator: IBookValidator = IBookValidator.default) {
  def isValidBook(name: String): Boolean = {
    validator.isNiceBook(name)
  }
}
