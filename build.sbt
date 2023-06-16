import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaSBT"
  )

javaOptions += s"-Dconfig.file=${sourceDirectory.value}/main/resources/application.conf"

// Workaround for NoSuchFieldException using withObjectMocked:
Test / javaOptions ++= Seq(
  "--add-opens=java.base/java.lang=ALL-UNNAMED",
  "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
)
Test / fork := true

libraryDependencies ++= dependencies

// tasks

lazy val hello = taskKey[Unit]("Prints 'Hello World'")

hello := println("hello world!")