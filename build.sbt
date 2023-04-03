import sbt.Keys.libraryDependencies
import dependencies._


/** ScalaVersion */
val scala3Version = "3.2.0"

/** Dependencies */
lazy val allDependencies = Seq(
 scalatic,
 scalatest,
 swing,
 ginject,
 well,
 xml,
 json,
 scalafx
)

/** Model Module */
lazy val model = (project in file("Model"))
  .settings(
    name := "Checkers-Model",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )




/** Persistence Module */
lazy val io = (project in file("IO"))
  .dependsOn(model)
  .settings(
    name := "Checkeres-IO",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** Helper Module */
lazy val helper = (project in file("Helper"))
  .settings(
    name := "Checkers-Helper",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
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
    libraryDependencies ++= allDependencies,
  )
  .enablePlugins(allDependencies)

/** Common Settings */
lazy val commonSettings = Seq(
  scalaVersion := scala3Version,
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