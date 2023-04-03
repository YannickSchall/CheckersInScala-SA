import sbt.Keys.libraryDependencies
/*
name := "CheckersInScala"
version := "0.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "com.google.inject" % "guice" % "5.1.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "5.1.1"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7"
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24"
scalaVersion := "3.2.0"
*/



/** ScalaVersion */
val scala3Version = "3.2.0"

/** Dependencies */
lazy val commonDependencies = Seq(
  dependencies.scalactic("3.2.15"),
  dependencies.scalatest("3.2.15"),
  dependencies.scalafx("16.0.0-R24"),
  dependencies.codingwell("5.1.1"),
  dependencies.googleinject,
  dependencies.scalalangmodules,
  dependencies.typesafeplay
)

/** Model Module */
lazy val model = (project in file("Model"))
  .settings(
    name := "Checkers-Model",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )

/** Persistence Module */
lazy val io = (project in file("IO"))
  .dependsOn(model)
  .settings(
    name := "Checkeres-IO",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )

/** Helper Module */
lazy val helper = (project in file("Helper"))
  .settings(
    name := "Checkers-Helper",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )

/** Root Module */
lazy val root = project
  .in(file("."))
  .dependsOn(helper, model, io)
  .aggregate(helper, model, io)
  .settings(
    name := "Checkers",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= commonDependencies,
  )
  .enablePlugins(JacocoCoverallsPlugin)

/** Common Settings */
lazy val commonSettings = Seq(
  scalaVersion := scala3Version,
  organization := "de.htwg.se",

  jacocoCoverallsServiceName := "github-actions",
  jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
  jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
  jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN"),
  jacocoExcludes in Test := Seq(
    "aview*",
    "fileIOComponent*",
    "Checkers"
  ),
  libraryDependencies ++= {
    // Determine OS version of JavaFX binaries
    lazy val osName = System.getProperty("os.name") match {
      case n if n.startsWith("Linux") => "linux"
      case n if n.startsWith("Mac") => "mac"
      case n if n.startsWith("Windows") => "win"
      case _ => throw new Exception("Unknown platform!")
    }

    Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
      .map(m => "org.openjfx" % s"javafx-$m" % "17.0.1" classifier osName)
  }
)