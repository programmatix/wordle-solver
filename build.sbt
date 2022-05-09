ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.0"

lazy val root = (project in file("."))
  .settings(
    name := "wordle-solver"
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"
