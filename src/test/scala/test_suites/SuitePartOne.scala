package test_suites

import org.scalatest.DoNotDiscover
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import test_suites.SuitePartOne.runCount

@DoNotDiscover
class SuitePartOne extends AnyFeatureSpec {
  Feature("Suite part") {
    Scenario("Should be run only inside suite") {
      runCount += 1
      runCount shouldEqual 1
    }
  }
}

object SuitePartOne {
  var runCount = 0
}
