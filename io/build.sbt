import sbt.Keys.libraryDependencies
import dependencies._


name := "Checkers-IO"
organization := "XXXY"
version := "0.5.0-SNAPSHOT"
scalaVersion := "3.2.0"

/** Dependencies */
lazy val allDependencies2 = Seq(
  scalatic,
  scalatest,
  swing,
  ginject,
  well,
  xml,
  json,
  scalafx,
  akkaHttp,
  akkaHttpSpray,
  akkaHttpCore,
  akkaActorTyped,
  akkaStream,
  akkaActor,
  slf4jNop,
  slick,
  hikarislick,
  mysql,
  mongoDB,
  mockito,
  gatlingHigh,
  gatlingTest
)

libraryDependencies ++= allDependencies2