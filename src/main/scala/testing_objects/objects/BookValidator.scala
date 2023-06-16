package testing_objects.objects

import testing_objects.objects.injection.IBookValidator

object BookValidator extends IBookValidator {
  def isNiceBook(bookName: String): Boolean = {
    BookRatingClient.getBookRating(bookName) > 5
  }
}
