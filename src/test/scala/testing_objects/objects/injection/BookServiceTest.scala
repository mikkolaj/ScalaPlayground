package testing_objects.objects.injection

import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class BookServiceTest extends AnyFeatureSpec with Matchers {

  Feature("IsValidBook") {
    Scenario("should return true if validator.isNiceBook() returns true") {
      val service = new BookService(MockBookValidator)

      service.isValidBook("Wiedzmin") shouldEqual true
    }
  }
}
