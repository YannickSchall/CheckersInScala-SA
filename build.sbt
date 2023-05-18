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
  slf4jNop,
  slick,
  hikarislick,
  mysql,
  mongoDB
)

/** Root Module */
lazy val root: Project = Project(id = "Checkers", base = file("."))
  .settings(
    name := "Checkers",
    version := "0.5.0-SNAPSHOT",
    commonSettings,
    libraryDependencies ++= allDependencies,
  )

/** IO Module */
lazy val io: Project = Project(id = "Checkers-IO", base = file("io"))
  .settings(
    name := "Checkers-IO",
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
