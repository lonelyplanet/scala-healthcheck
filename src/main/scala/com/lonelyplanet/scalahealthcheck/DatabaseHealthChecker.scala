package com.lonelyplanet.scalahealthcheck

trait DatabaseHealthChecker {
  def check: DatabaseHealth
}
