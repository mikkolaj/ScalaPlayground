package akka_http.basics

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka_http.basics.Completes.routes
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers

class CompletesTest extends AnyFeatureSpec with Matchers with ScalatestRouteTest {
  Feature("Paths") {
    Scenario("Should return appropriate responses") {
      Get("/a") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "foo"
      }

      Get("/b") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "OK"
      }

      Get("/c") ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] shouldEqual "bar"
      }

      Get("/d") ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] shouldEqual "bar"
      }

      Get("/e") ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        header[`Content-Type`] shouldEqual Some(`Content-Type`(`text/plain(UTF-8)`))
        responseAs[String] shouldEqual "bar"
      }

      Get("/f") ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        header[`Content-Type`] shouldEqual Some(`Content-Type`(`text/plain(UTF-8)`))
        responseAs[String] shouldEqual "bar"
      }

      Get("/g") ~> routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] shouldEqual "bar"
      }

      Get("/h") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "foo"
      }

      Get("/i") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "baz"
      }
    }
  }

}
