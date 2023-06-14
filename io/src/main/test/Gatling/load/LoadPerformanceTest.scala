import com.dimafeng.testcontainers.{ContainerDef, DockerComposeContainer, ExposedService}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.io.File
import org.testcontainers.containers.wait.strategy.Wait
import scala.concurrent.duration._

import util.data.Tile
import CheckersServiceSimulation._
import Database._

abstract class LoadPerformanceTest(database: Database) extends IOSimulation("Load", database):

  override protected val defaultUserCount: Int = 100
  override val scenarioBuilder = scenario(name)
    .feed(usernameFeeder)
    .feed(passwordFeeder)
    .exec(loadOperationChain)

  val extraScenario = scenario(name+"2")
    .feed(usernameFeeder)
    .feed(passwordFeeder)
    .exec(simpleOperationChain)

  override protected val populationBuilder =
    scenarioBuilder
      .inject(
        rampUsers(defaultUserCount).during(defaultRampDuration)
      ).andThen(
      extraScenario.inject(
        rampUsers(defaultUserCount).during(defaultRampDuration)
      )
    )


class MongoDbLoadPerformanceTest extends LoadPerformanceTest(MongoDb):
  setUp()

class MySqlLoadPerformanceTest extends LoadPerformanceTest(MySQL):
  setUp()