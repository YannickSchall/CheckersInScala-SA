
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.io.File
import org.testcontainers.containers.wait.strategy.Wait
import scala.concurrent.duration._

import util.data.Tile
import CheckersServiceSimulation._
import Database._


abstract class StressPerformanceTest(database: Database) extends IOSimulation("Stress", database):

  override protected val defaultUserCount: Int = 50
  override val scenarioBuilder = scenario(name)
    .feed(usernameFeeder)
    .feed(passwordFeeder)
    .exec(simpleOperationChain)

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