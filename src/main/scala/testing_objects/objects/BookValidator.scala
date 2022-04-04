package testing_objects.objects

object BookValidator {
  def isNiceBook(bookName: String): Boolean = {
    BookRatingClient.getBookRating(bookName) > 5
  }
}
