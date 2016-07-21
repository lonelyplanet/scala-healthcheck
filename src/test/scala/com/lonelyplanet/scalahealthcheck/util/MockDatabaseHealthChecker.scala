package com.lonelyplanet.scalahealthcheck.util

import com.lonelyplanet.scalahealthcheck.{DatabaseHealthCheckResult, HealthCheckResult, HealthChecker}

class MockDatabaseHealthChecker extends HealthChecker {
  var connectable = false

  override def check: HealthCheckResult = {
    DatabaseHealthCheckResult("localhost", 3306, connectable)
  }
}
