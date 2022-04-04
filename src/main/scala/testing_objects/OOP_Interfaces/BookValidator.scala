package testing_objects.OOP_Interfaces

trait BookValidator {
  def isNiceBook(bookName: String): Boolean
}

case object BookValidator {
  val defaultBookValidator = new BasicBookValidator
}
