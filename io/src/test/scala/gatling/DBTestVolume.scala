package gatling

import io.gatling.core.body.Body
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DBTestVolume extends Simulation {

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


  private val scn1 = scenario("RecordedSimulation1")
    .exec(requests.reduce((a, b) => a.pause(1.second).exec(b)))

  private val scn2 = scenario("RecordedSimulation2")
    .exec(requests.reduce((a, b) => a.pause(1.second).exec(b)))

  private val scn3 = scenario("RecordedSimulation3")
    .exec(requests.reduce((a, b) => a.pause(1.second).exec(b)))

  setUp(
    scn1.inject(
      //ramp up users to 100 in 10 seconds
      rampUsersPerSec(10) to 100 during 10.second
    ).andThen(
      scn2.inject(
        //hold 100 users for 10 seconds
        constantUsersPerSec(100) during 10.second
      )
    ).andThen(
      scn3.inject(
        //ramp down users to 0 in 10 seconds
        rampUsersPerSec(100) to 0 during 10.second
      )
    )
  ).protocols(httpProtocol)

}
