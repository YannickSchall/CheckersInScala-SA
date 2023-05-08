name := "Checkers-IO"
organization  := "XXXY"
version       := "0.5.0-SNAPSHOT"
scalaVersion := "3.2.0"

lazy val commonDependencies = Seq(
  dependencies.scalatic,
  dependencies.scalatest,
  dependencies.swing,
  dependencies.ginject,
  dependencies.well,
  dependencies.xml,
  dependencies.json,
  dependencies.scalafx,
  dependencies.akkaHttp,
  dependencies.akkaHttpSpray,
  dependencies.akkaHttpCore,
  dependencies.akkaActorTyped,
  dependencies.akkaStream,
  dependencies.akkaActor,
  dependencies.slf4jNop
)

libraryDependencies ++= commonDependencies