package testing_objects.OOP_Interfaces

class BasicBookRatingClient extends BookRatingClient {
  override def getBookRating(bookName: String): Int = {
    (math.random * 10).toInt
  }
}
