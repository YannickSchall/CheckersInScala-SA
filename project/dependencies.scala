import sbt._
import Keys._

object dependencies {
    val scalatic = "org.scalactic" %% "scalactic" % "3.2.15"
    val scalatest = "org.scalatest" %% "scalatest" % "3.2.15" % "test"
    val swing = "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
    val ginject = "com.google.inject" % "guice" % "5.1.0"
    val well = "net.codingwell" %% "scala-guice" % "5.1.1"
    val xml = "org.scala-lang.modules" %% "scala-xml" % "2.0.0"
    val json = "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
    val scalafx = "org.scalafx" %% "scalafx" % "16.0.0-R24"
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.2.9"
    val akkaHttpSpray = "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.9"
    val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % "10.2.9"
    val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % "2.6.19"
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.6.19"
    val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.6.19"
    val slf4jNop = "org.slf4j" % "slf4j-nop" % "2.0.0-alpha7"
}
