package com.lonelyplanet.scalahealthcheck.util

import com.lonelyplanet.scalahealthcheck.{DatabaseHealthCheckResult, HealthCheckResult, HealthChecker}

class AlwaysRedHealthCheck extends HealthChecker {
  override def check: HealthCheckResult = DatabaseHealthCheckResult("host", 3306, isDatabaseConnectable = false)
}
