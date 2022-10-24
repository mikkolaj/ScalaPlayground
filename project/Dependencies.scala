import sbt._

object Dependencies {
  private val akkaVersion = "2.6.19"
  private val akkaHttpVersion = "10.2.9"
  private val scalikejdbcVersion = "3.5.0"

  val dependencies: Seq[ModuleID] = Seq(
    // parallel collections
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",
    // akka typed
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    // akka streams
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    // akka http
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe" % "config" % "1.4.2",
    // DB connection
    "org.scalikejdbc" %% "scalikejdbc" % scalikejdbcVersion,
    "org.scalikejdbc" %% "scalikejdbc-interpolation" % scalikejdbcVersion,
    "org.scalikejdbc" %% "scalikejdbc-config" % scalikejdbcVersion,
    "org.postgresql" % "postgresql" % "42.3.6",
    // ORM
    "org.skinny-framework" %% "skinny-orm" % "3.1.0",
    // logging
    "org.slf4j" % "slf4j-simple" % "1.7.36",
    // testing
    "org.scalamock" %% "scalamock" % "5.2.0" % Test,
    "org.scalatest" %% "scalatest" % "3.2.11" % Test,
    "org.mockito" %% "mockito-scala" % "1.17.5" % Test
  )
}
