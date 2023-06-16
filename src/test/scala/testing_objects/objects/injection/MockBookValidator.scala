package testing_objects.objects.injection

object MockBookValidator extends IBookValidator {
  override def isNiceBook(name: String): Boolean = true
}
