package gatling

import io.gatling.core.body.Body
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DBTestLoad extends Simulation {

  private val requests = List(
    createRequest("DBSave", "PUT", "/io/dbsave", RawFileBody("game.json")),
    createRequest("DBLoad", "GET", "/io/dbload", StringBody("")),
  )

  def createRequest(name: String, request: String, operation: String, body: Body): ChainBuilder = {
  exec(
    http(name)
      .httpRequest(request, operation)
      .body(body)
  )
}
  private val httpProtocol = http
    .baseUrl("http://localhost:8081")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .userAgentHeader("PostmanRuntime/7.32.2")


  private val scn = scenario("RecordedSimulation")
    .exec(requests.reduce((a, b) => a.pause(1.second).exec(b)))

  setUp(
    scn.inject(
      rampUsers(10) during 20.seconds
    )
  ).protocols(httpProtocol)

}
