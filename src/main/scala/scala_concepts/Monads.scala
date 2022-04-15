package scala_concepts

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

// tutorial: https://www.youtube.com/watch?v=d-dy1x33moA

object Monads extends App {
  case class SafeValue[+T](private val internalValue: T) { // "constructor" = pure, unit
    def get: T = synchronized {
      internalValue
    }

    def flatMap[S](transformer: T => SafeValue[S]): SafeValue[S] = synchronized { // bind, flatMap
      transformer(internalValue)
    }
  }

  // imitates external API
  def gimmeSafeValue[T](value: T): SafeValue[T] = SafeValue(value)

  val safeString: SafeValue[String] = gimmeSafeValue("Some string")

  // ETW pattern
  // extract
  val string = safeString.get
  // transform
  val upperString = string.toUpperCase
  // wrap again
  val upperSafeString = SafeValue(upperString)

  // compressed ETW
  val upperSafeString2 = safeString.flatMap(string => SafeValue(string.toUpperCase))

  // Examples

  // Example 1: census
  case class Person(firstName: String, lastName: String) {
    assert(firstName != null && lastName != null)
  }

  // census API
  def getPerson(firstName: String, lastName: String): Person = {
    // ifs not joined together on purpose - to show pattern of extracting/checking values one by one
    if (firstName != null) { // extracting first value
      if (lastName != null) { // extracting second value
        Person(firstName, lastName) // if they both exist return person
      } else {
        null
      }
    } else {
      null
    }
  }

  def getPersonWithOption(firstName: String, lastName: String): Option[Person] = {
    Option(firstName).flatMap { fName =>
      Option(lastName).flatMap { lName =>
        Option(Person(fName, lName))
      }
    }
  }

  def getPersonWithForComprehension(firstName: String, lastName: String): Option[Person] = {
    for {
      fName <- Option(firstName)
      lName <- Option(lastName)
    } yield Person(fName, lName)
  }

  // Example 2: asynchronous fetches

  val VatMultiplier = 1.19

  case class User(id: String)

  case class Product(sku: String, price: Double)

  def getUser(url: String): Future[User] = Future {
    User("Mikolaj")
  }

  def getLastOrder(userId: String): Future[Product] = Future {
    Product("123456", 55.55)
  }

  val myUrl = "mystore.com/users/mikolaj"

  // ETW
  getUser(myUrl).onComplete {
    case Success(User(id)) =>
      val lastOrder = getLastOrder(id)
      lastOrder.onComplete {
        case Success(Product(sku, price)) =>
          val vatIncludedPrice = price * VatMultiplier
        // do whatever you like with that price
      }
  }

  val vatIncludedPrice: Future[Double] = getUser(myUrl)
    .flatMap(user => getLastOrder(user.id))
    .map(_.price * VatMultiplier)

  val vatIncludedPriceWithForComprehension: Future[Double] = for {
    user <- getUser(myUrl)
    order <- getLastOrder(user.id)
  } yield order.price * VatMultiplier

  // Example 3: double-for loops

  val numbers = List(1, 2, 3)
  val chars = List('a', 'b', 'c')

  val checkerboard: List[(Int, Char)] = numbers.flatMap(number => chars.map(char => (number, char)))

  val checkerboardWithForComprehension: List[(Int, Char)] = for {
    number <- numbers
    char <- chars
  } yield (number, char)

  // Properties of Monads

  // 1. Left identity
  // Applying function returning monad X to an element yields the same result as flatMaping
  // a monad X containing this element
  // Monad(x).flatMap(f) == f(x)

  def twoConsecutive(x: Int) = List(x, x + 1)

  twoConsecutive(3) // List(3,4)
  List(3).flatMap(twoConsecutive) // List(3,4)

  // 2. Right identity
  // flatMapping a monad X with a function that only wraps it's elements in a monad X
  // doesn't do anything
  // Monad(v).flatMap(x => Monad(x)) == Monad(v)
  List(1, 2, 3).flatMap(x => List(x)) // List(1, 2, 3)

  // 3. Associativity (ETW-ETW)
  // Monad(v).flatMap(f).flatMap(g) == Monad(v).flatMap(x => f(x).flatMap(g))
  val incrementer = (x: Int) => List(x, x + 1)
  val doubler = (x: Int) => List(x, x * 2)

  println(numbers.flatMap(incrementer).flatMap(doubler))
  println(numbers.flatMap(x => incrementer(x).flatMap(doubler)))

  /* List(1, 2, 2, 4,    2, 4, 3, 6,    3, 6, 4, 8)
    List(
      incrementer(1).flatMap(doubler) -- 1, 2, 2, 4
      incrementer(2).flatMap(doubler) -- 2, 4, 3, 6
      incrementer(3).flatMap(doubler) -- 3, 6, 4, 8
    )
   */
}
