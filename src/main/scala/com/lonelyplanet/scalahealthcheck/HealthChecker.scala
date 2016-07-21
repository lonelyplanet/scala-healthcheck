package com.lonelyplanet.scalahealthcheck

trait HealthChecker {
  def check: HealthCheckResult
}
