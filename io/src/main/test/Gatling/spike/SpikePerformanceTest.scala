
import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.io.File
import org.testcontainers.containers.wait.strategy.Wait
import scala.concurrent.duration._

import util.data.Tile
import ChessServiceSimulation._
import Database._

abstract class SpikePerformanceTest(database: Database) extends IOSimulation("Spike", database):

  override protected val defaultUserCount: Int = 300
  override val scenarioBuilder = scenario(name)
    .feed(usernameFeeder)
    .feed(passwordFeeder)
    .exec(simpleOperationChain)

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


class MongoDbSpikePersistenceSimulation extends SpikePerformanceTest(MongoDb):
  setUp()

class PostgresSpikePersistenceSimulation extends SpikePerformanceTest(MySQL):
  setUp()