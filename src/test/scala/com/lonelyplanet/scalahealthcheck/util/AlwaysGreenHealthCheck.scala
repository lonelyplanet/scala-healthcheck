package com.lonelyplanet.scalahealthcheck.util

import com.lonelyplanet.scalahealthcheck.{DatabaseHealthCheckResult, HealthCheckResult, HealthChecker}

class AlwaysGreenHealthCheck extends HealthChecker {
  override def check: HealthCheckResult = DatabaseHealthCheckResult("host", 3306, isDatabaseConnectable = true)
}
