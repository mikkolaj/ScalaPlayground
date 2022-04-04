package testing_objects.OOP_Interfaces

trait BookRatingClient {
  def getBookRating(bookName: String): Int
}

case object BookRatingClient {
  val defaultBookRatingClient = new BasicBookRatingClient
}