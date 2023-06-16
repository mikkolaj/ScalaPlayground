package testing_objects.OOP_Interfaces

class BasicBookValidator(bookRatingClient: BookRatingClient = BookRatingClient.defaultBookRatingClient)
    extends BookValidator {
  def isNiceBook(bookName: String): Boolean = {
    bookRatingClient.getBookRating(bookName) > 5
  }
}
