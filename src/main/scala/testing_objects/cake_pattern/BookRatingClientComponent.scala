package testing_objects.cake_pattern

trait BookRatingClientComponent {
  val bookRatingClient: BookRatingClient

  trait BookRatingClient {
    def getBookRating(bookName: String): Int
  }
}
