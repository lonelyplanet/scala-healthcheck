package com.lonelyplanet.scalahealthcheck

abstract class HealthCheckResult(val location: String, val isServiceOk: Boolean, val message: String)

case class DatabaseHealthCheckResult(override val location: String, override val isServiceOk: Boolean, override val message: String)
  extends HealthCheckResult(location, isServiceOk, message)

object DatabaseHealthCheckResult {
  private val DatabaseConnectableMessage = "No problems found"
  private val DatabaseNotConnectableMessage = "Could not connect to database"

  def apply(host: String, port: Int, isGreenFunction: => Boolean): DatabaseHealthCheckResult = {
    val isServiceOk = isGreenFunction
    val message = if (isServiceOk) DatabaseConnectableMessage else DatabaseNotConnectableMessage

    DatabaseHealthCheckResult(
      location = s"$host:$port",
      isServiceOk = isServiceOk,
      message = message
    )
  }
}

case class WebServiceHealthCheckResult(override val location: String, override val isServiceOk: Boolean, override val message: String)
  extends HealthCheckResult(location, isServiceOk, message)

object WebServiceHealthCheckResult {
  private val greenMessage = "No problems found"
  private val redMessage = "Problem with service"

  def apply(host: String, port: Int, isGreenFunction: => Boolean): WebServiceHealthCheckResult = {
    val isServiceOk = isGreenFunction
    val message = if (isServiceOk) greenMessage else redMessage

    WebServiceHealthCheckResult(
      location = s"$host:$port",
      isServiceOk = isServiceOk,
      message = message
    )
  }
}