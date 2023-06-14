package gatling

import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.core.body.Body
import io.gatling.core.structure.ChainBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.netty.handler.codec.http.HttpMethod

import java.io.File
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.duration.*
import util.*
import CheckersServiceSimulation.*
import io.gatling.core.Predef.feed
import io.gatling.javaapi.core.CoreDsl.tryMax

enum Database(val name: String, val port: Int):
  case MongoDb extends Database("mongo-db", -1)
  case MySQL extends Database("mysql", 3306)

  override def toString(): String = name

abstract class IoSimulation(
                                      protected val  name: String,
                                      database: Database
                                    ) extends CheckersServiceSimulation(
  name = s"${database}${name}IOSimulation",
  serviceUrl = "http://0.0.0.0:8081",
  dockerComposeFile =
    new File(s"src/test/resources/docker/docker-compose-$database.yaml"),
  exposedServices =
    Map(database.name -> database.port, "io" -> 8081)
):

  override protected val defaultPauseDuration = 1.seconds

  //ACHTUNG SAVE VOR DELETE
  protected val save = tryMax(3) {
    feed(randomFeeder)
      .exec(
        buildOperation(
          name = "save gameboard",
          path = "/io/save",
          method = HttpMethod.POST,
          body = StringBody("#{gameboard}")
        )
      ).exitHereIf { session =>
      session("errorBodyString").asOption[String].isDefined
    }
  }.exitHereIfFailed
  protected val dbsave = tryMax(3) {
    feed(randomFeeder)
      .exec(
        buildOperation(
          name = "save gameboard",
          path = "/io/dbsave",
          method = HttpMethod.POST,
          body = StringBody("#{gameboard}")
        )
      ).exitHereIf { session =>
      session("errorBodyString").asOption[String].isDefined
    }
  }.exitHereIfFailed

  protected val load = tryMax(3) {
    buildOperation(
      name = "io load",
      path = s"/io/load",
      method = HttpMethod.GET
    ).exitHereIf { session =>
      session("errorBodyString").asOption[String].isDefined
    }
  }.exitHereIfFailed
  protected val dbload = tryMax(3) {

    buildOperation(
      name = "io dbload",
      path = s"/io/dbload?id=#{id}",
      method = HttpMethod.GET
    ).exitHereIf { session =>
      session("errorBodyString").asOption[String].isDefined
    }
  }.exitHereIfFailed

  protected val dbdelete = feed(randomFeeder)
    .exec(
      buildOperation(
        name = "delete db",
        path = "/io/dbdelete?id=#{id}", // TODO: woher kommt die ID
        method = HttpMethod.POST,
        body = StringBody("#{gameboard}")
      )
    )
  protected val dbupdate = feed(randomFenFeeder)
    .exec(
      buildOperation(
        name = "update db via id",
        path = "/io/dbupdate?id=#{id}",
        method = HttpMethod.POST,
        body = StringBody("#{fen}")
      )
    )




  protected val operationChain = exec(
    save,
    dbsave,
    load,
    dbload,
    dbupdate,
    dbdelete
  )