package com.lonelyplanet.scalahealthcheck

import _root_.akka.http.scaladsl.testkit.ScalatestRouteTest
import com.lonelyplanet.scalahealthcheck.util.{AlwaysGreenHealthCheck, AlwaysRedHealthCheck}
import org.scalatest.{FlatSpec, Matchers}

class HealthCheckSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  val alwaysGreen = new AlwaysGreenHealthCheck
  val alwaysRed = new AlwaysRedHealthCheck

  it should "be return correct messages" in {
    alwaysGreen.check.message shouldBe "No problems found"
    alwaysRed.check.message shouldBe "Could not connect to database"
  }

  it should "return dependency with health check result" in {
    val dbDependency = DatabaseServiceDependency(new AlwaysGreenHealthCheck, "green-db")
    dbDependency.healthChecker.check.isConnectable shouldBe true
  }
}
