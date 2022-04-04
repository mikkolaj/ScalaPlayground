package testing_objects.OOP_Simple

class BookValidator(bookRatingClient: BookRatingClient = BookRatingClient.defaultBookRatingClient) {
  def isNiceBook(bookName: String): Boolean = {
    bookRatingClient.getBookRating(bookName) > 5
  }
}

case object BookValidator {
  val defaultBookValidator = new BookValidator
}
