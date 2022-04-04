package testing_objects.cake_pattern

import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper


class BookValidatorTest extends AnyFeatureSpec with GivenWhenThen with MockFactory {
  class LowestBookRatingValidator extends BookValidator with LowestBookRatingClientComponent

  class HighestBookRatingValidator extends BookValidator with HighestBookRatingClientComponent

  trait LowestBookRatingClientComponent extends BookRatingClientComponent {
    val bookRatingClient = new HighestBookRatingClient

    class HighestBookRatingClient extends BookRatingClient {
      override def getBookRating(bookName: String): Int = 0
    }
  }

  trait HighestBookRatingClientComponent extends BookRatingClientComponent {
    val bookRatingClient = new HighestBookRatingClient

    class HighestBookRatingClient extends BookRatingClient {
      override def getBookRating(bookName: String): Int = 10
    }
  }

  Feature("BookValidator") {
    Scenario("Return true when rest returns > 5") {
      Given("Lowest possible book rating results")

      val validator = new LowestBookRatingValidator

      When("Book gets validated")
      val result = validator.isNiceBook("bla")

      Then("Book should not be validated")
      result shouldBe false
    }

    Scenario("Return false when rest returns <= 5") {
      Given("Lowest possible book rating results")
      val validationTester = new HighestBookRatingValidator

      When("Book gets validated")
      val result = validationTester.isNiceBook("fdsa")

      Then("Book should not be validated")
      result shouldBe true
    }
  }

}
