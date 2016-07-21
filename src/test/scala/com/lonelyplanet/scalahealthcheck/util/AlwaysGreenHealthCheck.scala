package com.lonelyplanet.scalahealthcheck.util

import com.lonelyplanet.scalahealthcheck.{DatabaseHealth, DatabaseHealthChecker}

class AlwaysGreenHealthCheck extends DatabaseHealthChecker {
  override def check: DatabaseHealth = DatabaseHealth("host", 3306, isConnectable = true)
}
