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
  scalafx,
  akkaHttp,
  akkaHttpSpray,
  akkaHttpCore,
  akkaActorTyped,
  akkaStream,
  akkaActor,
  slf4jNop
)

/** Root Module */
lazy val root: Project = Project(id = "Checkers", base = file("."))
  .dependsOn(model, helper, io, view)
  .settings(
    name := "Checkers",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** IO Module */
lazy val io: Project = Project(id = "Checkers-IO", base = file("io"))
  .dependsOn(model)
  .settings(
    name := "Checkers-IO",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** Model Module */
lazy val model: Project = Project(id = "Checkers-Model", base = file("model"))
  .dependsOn(helper)
  .settings(
    name := "Checkers-Model",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** Helper Module */
lazy val helper: Project = Project(id = "Checkers-Helper", base = file("helper"))
  .settings(
    name := "Checkers-Helper",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** View Module */
lazy val view: Project = Project(id = "Checkers-View", base = file("view"))
  .settings(
    name := "Checkers-View",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** Common Settings */
lazy val commonSettings: Seq[Def.Setting[_]] = Seq(
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
