package simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MeasurementsGetSimulation extends Simulation {

  private val users = sys.env("CONSTANT_USERS").toDouble

  private val duration = sys.env("DURATION").toInt

  private val endpoint = sys.env("ENDPOINT")

  private val httpConf = http.baseUrl(endpoint)

  private val scn = scenario("measurements")
    .exec(
      http("GET /measurements")
        .get(s"/measurements/id")
        .headers(Map("Accept" -> "application/x-msgpack")))

  setUp(
    scn
      .inject(constantUsersPerSec(users).during(duration))
      .protocols(httpConf))
}
