package test_suites

import org.scalatest.{BeforeAndAfterAll, Suites}

class TestingSuite extends Suites(new SuitePartOne, new SuitePartTwo) with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    println("Before the whole suite!")
  }

  override def afterAll(): Unit = {
    println("After the whole suite!")
  }
}