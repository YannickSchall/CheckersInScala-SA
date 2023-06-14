package gatling
package stress

import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.http.Predef.*

import java.io.File
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.duration.*
import util.*
import gatling.CheckersServiceSimulation.*
import gatling.Database.*
import io.gatling.core.Predef.*


abstract class StressPerformanceTest(database: Database) extends IoSimulation("Stress", database):

  override protected val defaultUserCount: Int = 50
  override val scenarioBuilder = scenario(name)
    .feed(idFeeder)
    .exec(operationChain)

  override protected val populationBuilder =
    scenarioBuilder
      .inject(incrementConcurrentUsers(defaultUserCount)
        .times(5)
        .eachLevelLasting(defaultRampDuration / 4)
        .separatedByRampsLasting(defaultRampDuration / 4)
        .startingFrom(10)
      ).disablePauses


class MongoDbLoadPerformanceTest extends StressPerformanceTest(MongoDb):
  setUp()

class MySqlLoadPerformanceTest extends StressPerformanceTest(MySQL):
  setUp()