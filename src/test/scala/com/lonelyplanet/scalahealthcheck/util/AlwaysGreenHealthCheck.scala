package com.lonelyplanet.scalahealthcheck.util

import com.lonelyplanet.scalahealthcheck.{HealthCheckResult, HealthChecker}

class AlwaysGreenHealthCheck extends HealthChecker {
  override def check: HealthCheckResult = HealthCheckResult("host", 3306, isConnectable = true)
}
