import sbt._
import sbt.librarymanagement.InclExclRule




import Keys._

object dependencies {
  val scalatic = "org.scalactic" %% "scalactic" % "3.2.15"
  val scalatest = "org.scalatest" %% "scalatest" % "3.2.15" % "test"
  val swing = ("org.scala-lang.modules" %% "scala-swing" % "3.0.0").cross(CrossVersion.for3Use2_13)
  val ginject = "com.google.inject" % "guice" % "5.1.0"
  val well = "net.codingwell" %% "scala-guice" % "5.1.1"
  val xml = "org.scala-lang.modules" %% "scala-xml" % "2.0.0"
  val json = "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
  val scalafx = "org.scalafx" %% "scalafx" % "16.0.0-R24"
  val akkaHttp = ("com.typesafe.akka" %% "akka-http" % "10.2.9").cross(CrossVersion.for3Use2_13)
  val akkaHttpSpray = ("com.typesafe.akka" %% "akka-http-spray-json" % "10.2.9").cross(CrossVersion.for3Use2_13)
  val akkaHttpCore = ("com.typesafe.akka" %% "akka-http-core" % "10.2.9").cross(CrossVersion.for3Use2_13)
  val akkaActorTyped = ("com.typesafe.akka" %% "akka-actor-typed" % "2.6.19").cross(CrossVersion.for3Use2_13)
  val akkaStream = ("com.typesafe.akka" %% "akka-stream" % "2.6.19").cross(CrossVersion.for3Use2_13)
  val akkaActor = ("com.typesafe.akka" %% "akka-actor" % "2.6.19").cross(CrossVersion.for3Use2_13)
  val slf4jNop = "org.slf4j" % "slf4j-nop" % "2.0.0-alpha7"
  val slick = ("com.typesafe.slick" %% "slick" % "3.5.0-M3").cross(CrossVersion.for3Use2_13)
  val hikarislick = ("com.typesafe.slick" %% "slick-hikaricp" % "3.5.0-M3").cross(CrossVersion.for3Use2_13)
  val mysql = "mysql" % "mysql-connector-java" % "8.0.32"
  val mongoDB = ("org.mongodb.scala" %% "mongo-scala-driver" % "4.9.1").cross(CrossVersion.for3Use2_13)
  val mockito = "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % Test
  val gatlingExclude = Seq(("com.typesafe.akka", "akka-actor_2.13"), ("org.scala-lang.modules", "scala-java8-compat_2.13"), ("com.typesafe.akka", "akka-slf4j_2.13")).toVector.map((org_name: Tuple2[String, String]) => InclExclRule(org_name._1, org_name._2))
  val gatlingHigh = ("io.gatling.highcharts" % "gatling-charts-highcharts" % "3.9.5" % "test").withExclusions(gatlingExclude)
  val gatlingTest = ("io.gatling" % "gatling-test-framework" % "3.9.5" % "test").withExclusions(gatlingExclude)
  val testcontainer  = "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.15"
  val testkit = ("com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.19" % Test).cross(CrossVersion.for3Use2_13)

}
