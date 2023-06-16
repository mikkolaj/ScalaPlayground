package testing_objects.objects.injection

import testing_objects.objects.BookValidator

trait IBookValidator {
  def isNiceBook(name: String): Boolean
}

object IBookValidator {
  lazy val default: IBookValidator = BookValidator
}
