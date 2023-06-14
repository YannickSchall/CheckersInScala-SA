package gatling

import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}

import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.http.Predef.*

import java.io.File
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.duration.*
import util.*
import gatling.CheckersServiceSimulation.*
import gatling.Database.*
import io.gatling.core.Predef.*

abstract class LoadPerformanceTest(database: Database) extends IoSimulation("Load", database):

  override protected val defaultUserCount: Int = 100
  override val scenarioBuilder = scenario(name)
  .feed(idFeeder)
  .exec(operationChain)



  override protected val populationBuilder =
    scenarioBuilder
      .inject(
        rampUsers(defaultUserCount).during(defaultRampDuration)
      )


class MongoDbLoadPerformanceTest extends LoadPerformanceTest(MongoDb):
  setUp()

class MySqlLoadPerformanceTest extends LoadPerformanceTest(MySQL):
  setUp()