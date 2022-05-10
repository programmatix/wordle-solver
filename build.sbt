ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.0"

lazy val root = (project in file("."))
  .settings(
    name := "wordle-solver"
  )

// Testing
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"
// Command line parsing
libraryDependencies += "com.monovore" %% "decline" % "2.2.0"
