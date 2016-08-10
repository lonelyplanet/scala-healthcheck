package com.lonelyplanet.scalahealthcheck

class HealthCheckSpec extends BaseSpec {

  it should "be return correct messages" in {
    alwaysGreenHealthCheck.check.message shouldBe "No problems found"
    alwaysRedHealthCheck.check.message shouldBe "Could not connect to database"
  }

  it should "return dependency with health check result" in {
    val dbDependency = DatabaseServiceDependency(alwaysGreenHealthCheck, "green-db")
    dbDependency.healthChecker.check.isServiceOk shouldBe true
  }
}
