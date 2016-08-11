package com.lonelyplanet.scalahealthcheck

import org.scalatest.{FlatSpec, Matchers}
import _root_.akka.http.scaladsl.testkit.ScalatestRouteTest
import com.lonelyplanet.scalahealthcheck.akka.http.routes.HealthCheckApiRoutes
import com.lonelyplanet.scalahealthcheck.util.{AlwaysGreenHealthCheck, AlwaysRedHealthCheck}
import scala.collection.immutable.Seq
import _root_.akka.http.scaladsl.model.StatusCodes.ServiceUnavailable

class BaseSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  val alwaysGreenHealthCheck = new AlwaysGreenHealthCheck
  val alwaysRedHealthCheck = new AlwaysRedHealthCheck

  val redApi = new HealthCheckApiRoutes(
    Seq(DatabaseServiceDependency(new AlwaysRedHealthCheck, "red-db")),
    "health-check",
    ServiceUnavailable
  )

  val greenApi = new HealthCheckApiRoutes(
    Seq(DatabaseServiceDependency(new AlwaysGreenHealthCheck, "green-db")),
    "health-check",
    ServiceUnavailable
  )

}
