package testing_objects.objects

object BookRatingClient {
  def getBookRating(bookName: String): Int = {
    (math.random * 10).toInt
  }
}
