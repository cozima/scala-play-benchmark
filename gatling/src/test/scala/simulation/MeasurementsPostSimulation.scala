package simulation

import controllers.MessagepackSupport
import io.circe.Json
import io.circe.parser.parse
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.io.Source

class MeasurementsPostSimulation extends Simulation with MessagepackSupport {

  private val users = sys.env("CONSTANT_USERS").toDouble

  private val duration = sys.env("DURATION").toInt

  private val endpoint = sys.env("ENDPOINT")

  private val measurement: String =
    Source
      .fromInputStream(getClass.getResourceAsStream("/fixtures/post.json"))
      .mkString
      .stripMargin

  private val json: Json = parse(measurement).getOrElse(Json.Null)

  private val body: Array[Byte] = msgpack(json)

  private val httpConf = http.baseUrl(endpoint)

  private val scn = scenario("measurements")
    .exec(
      http("POST /measurements")
        .post("/measurements")
        .headers(
          Map(
            "Content-Type" -> "application/x-msgpack",
            "Accept"       -> "application/x-msgpack"
          ))
        .body(ByteArrayBody(body))
    )

  setUp(
    scn
      .inject(constantUsersPerSec(users).during(duration))
      .protocols(httpConf))
}
