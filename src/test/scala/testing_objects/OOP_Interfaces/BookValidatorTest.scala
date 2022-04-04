package testing_objects.OOP_Interfaces

import org.scalamock.scalatest.MockFactory
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper


class BookValidatorTest extends AnyFeatureSpec with GivenWhenThen with MockFactory {
  val BookName = "test"

  Feature("BookValidator") {
    Scenario("Return true when rest returns > 5") {
      Given("Lowest possible book rating results")
      val bookRatingClient = mock[BookRatingClient]

      (bookRatingClient.getBookRating _).expects(BookName).returns(0)

      val validator = new BasicBookValidator(bookRatingClient)

      When("Book gets validated")
      val result = validator.isNiceBook(BookName)

      Then("Book should not be validated")
      result shouldBe false
    }

    Scenario("Return false when rest returns <= 5") {
      Given("Lowest possible book rating results")
      val bookRatingClient = mock[BookRatingClient]

      (bookRatingClient.getBookRating _).expects(BookName).returns(10)

      val validator = new BasicBookValidator(bookRatingClient)

      When("Book gets validated")
      val result = validator.isNiceBook(BookName)

      Then("Book should not be validated")
      result shouldBe true
    }
  }

}
