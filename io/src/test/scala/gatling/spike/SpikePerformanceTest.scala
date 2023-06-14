package gatling

import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.http.Predef.*

import java.io.File
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.duration.*
import util.*
import gatling.CheckersServiceSimulation.*
import gatling.Database.*
import io.gatling.core.Predef.*

abstract class SpikePerformanceTest(database: Database) extends IoSimulation("Spike", database):

  override protected val defaultUserCount: Int = 300
  override val scenarioBuilder = scenario(name)
    .feed(idFeeder)
    .exec(operationChain)

  override protected val populationBuilder =
    scenarioBuilder
      .inject(
        constantUsersPerSec(defaultUserCount / 10).during(10.seconds),
        nothingFor(3.seconds),
        atOnceUsers(defaultUserCount),
        nothingFor(15.seconds),
        atOnceUsers(defaultUserCount),
        nothingFor(30.seconds),
        stressPeakUsers(defaultUserCount * 4).during(defaultRampDuration / 2),
      )


class MongoDbLoadPerformanceTest extends SpikePerformanceTest(MongoDb):
  setUp()

class MySqlLoadPerformanceTest extends SpikePerformanceTest(MySQL):
  setUp()