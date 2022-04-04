package testing_objects.cake_pattern

trait BasicBookRatingClientComponent extends BookRatingClientComponent {
  val bookRatingClient = new BasicBookRatingClient

  class BasicBookRatingClient extends BookRatingClient {
    def getBookRating(bookName: String): Int = {
      (math.random * 10).toInt
    }
  }
}
