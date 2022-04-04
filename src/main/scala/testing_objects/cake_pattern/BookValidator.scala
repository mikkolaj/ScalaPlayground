package testing_objects.cake_pattern

class BookValidator {
  this: BookRatingClientComponent =>
  def isNiceBook(bookName: String): Boolean = {
    bookRatingClient.getBookRating(bookName) > 5
  }
}

case object BookValidator {
  val defaultValidator = new BookValidator with BasicBookRatingClientComponent
}
