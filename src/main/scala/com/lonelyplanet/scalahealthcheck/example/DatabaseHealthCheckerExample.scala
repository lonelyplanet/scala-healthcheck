package com.lonelyplanet.scalahealthcheck.example

import com.lonelyplanet.scalahealthcheck._

class DatabaseHealthCheckerExample(serverName: String, port: Int) extends HealthChecker {
  private val r = scala.util.Random
  private def checkConnectivity = {
    val rand = r.nextInt(100)
    rand > 50
  }

  def check: HealthCheckResult = HealthCheckResult(serverName, port, isConnectable = checkConnectivity)
}
