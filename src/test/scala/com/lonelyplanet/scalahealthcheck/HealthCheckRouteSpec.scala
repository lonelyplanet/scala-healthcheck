package com.lonelyplanet.scalahealthcheck

import _root_.akka.http.scaladsl.testkit.ScalatestRouteTest
import com.lonelyplanet.scalahealthcheck.akka.http.routes.HealthCheckApiRoutes
import com.lonelyplanet.scalahealthcheck.util.{AlwaysGreenHealthCheck, AlwaysRedHealthCheck}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.Seq
import _root_.akka.http.scaladsl.model.StatusCodes.OK
import com.lonelyplanet.scalahealthcheck.fixtures.TestResponses
import spray.json._

class HealthCheckRouteSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  it should "return single health check" in {
    val healthCheckApi = new HealthCheckApiRoutes(
      Seq(DatabaseServiceDependency(new AlwaysGreenHealthCheck, "green-db")),
      "health-check"
    )

    Get("/health-check") ~> healthCheckApi.routes ~> check {
      status shouldBe OK
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.singleHealthCheckDependencies
    }

    Get("/health-check?include=dependencies") ~> healthCheckApi.routes ~> check {
      status shouldBe OK
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.singleHealthCheckWithDependencies
    }
  }

  it should "return multiple health checks" in {
    val healthCheckApi = new HealthCheckApiRoutes(
      Seq(
        DatabaseServiceDependency(new AlwaysGreenHealthCheck, "green-db"),
        DatabaseServiceDependency(new AlwaysRedHealthCheck, "red-db")
      ),
      "health-check"
    )

    Get("/health-check") ~> healthCheckApi.routes ~> check {
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.multipleHealthChecksNoDependencies
      status shouldBe OK
    }

    Get("/health-check?include=dependencies") ~> healthCheckApi.routes ~> check {
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.multipleHealthChecksWithDependencies
      status shouldBe OK
    }
  }
}
