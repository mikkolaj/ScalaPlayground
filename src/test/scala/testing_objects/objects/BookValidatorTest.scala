package testing_objects.objects

import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.withObjectMocked
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

// Workaround for NoSuchFieldException:
// --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
class BookValidatorTest extends AnyFeatureSpec with GivenWhenThen {
  val BookName = "test"

  Feature("BookValidator") {
    Scenario("Return true when rest returns > 5") {
      Given("Lowest possible book rating results")

      // NoSuchFieldException, https://github.com/mockito/mockito-scala/issues/350
      withObjectMocked[BookRatingClient.type] {
        BookRatingClient.getBookRating(BookName) returns 0

        BookRatingClient.getBookRating(BookName) shouldBe 0

        When("Book gets validated")
        val result = BookValidator.isNiceBook(BookName)

        Then("Book should not be validated")
        result shouldBe false
      }
    }

    Scenario("Return false when rest returns <= 5") {
      Given("Lowest possible book rating results")

      // NoSuchFieldException, https://github.com/mockito/mockito-scala/issues/350
      withObjectMocked[BookRatingClient.type] {
        BookRatingClient.getBookRating(BookName) returns 10

        BookRatingClient.getBookRating(BookName) shouldBe 10

        When("Book gets validated")
        val result = BookValidator.isNiceBook(BookName)

        Then("Book should not be validated")
        result shouldBe true
      }

    }
  }
}
