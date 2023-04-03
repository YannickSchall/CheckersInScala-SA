import sbt._
import Keys._
object Dependecies {
  val scalatic = "org.scalactic" %% "scalactic" % "3.2.15"
  val scalatest = "org.scalatest" %% "scalatest" % "3.2.15" % "test"
  val swing = "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
  val ginject = "com.google.inject" % "guice" % "5.1.0"
  val well = "net.codingwell" %% "scala-guice" % "5.1.1"
  val xml = "org.scala-lang.modules" %% "scala-xml" % "2.0.0"
  val json = "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
  val scalafx = "org.scalafx" %% "scalafx" % "16.0.0-R24"
}
