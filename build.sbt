ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaSBT"
  )

val akkaVersion = "2.6.19"
val akkaHttpVersion = "10.2.9"

javaOptions += s"-Dconfig.file=${sourceDirectory.value}/main/resources/application.conf"

// Workaround for NoSuchFieldException using withObjectMocked:
Test / javaOptions ++= Seq(
  "--add-opens=java.base/java.lang=ALL-UNNAMED",
  "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
)
Test / fork := true

libraryDependencies ++= Seq(
  // akka typed
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  // akka streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe" % "config" % "1.4.2",

  // DB connection
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
  "org.postgresql" % "postgresql" % "42.3.6",

  // logging
  "org.slf4j" % "slf4j-simple" % "1.7.36",

  // testing
  "org.scalamock" %% "scalamock" % "5.2.0" % Test,
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "org.mockito" %% "mockito-scala" % "1.17.5" % Test,
)

