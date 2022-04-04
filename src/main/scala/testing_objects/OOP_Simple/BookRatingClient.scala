package testing_objects.OOP_Simple

class BookRatingClient {
  def getBookRating(bookName: String): Int = {
    (math.random * 10).toInt
  }
}

case object BookRatingClient {
  val defaultBookRatingClient = new BookRatingClient
}