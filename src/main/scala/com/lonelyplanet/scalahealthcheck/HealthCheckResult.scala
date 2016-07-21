package com.lonelyplanet.scalahealthcheck

case class HealthCheckResult(location: String, isConnectable: Boolean, message: String)

object HealthCheckResult {
  private val DatabaseConnectableMessage = "No problems found"
  private val DatabaseNotConnectableMessage = "Could not connect to database"

  def apply(databaseHost: String, databasePort: Int, isConnectable: => Boolean): HealthCheckResult = {
    val didManageToConnect = isConnectable
    val message = if (didManageToConnect) DatabaseConnectableMessage else DatabaseNotConnectableMessage

    HealthCheckResult(
      location = s"$databaseHost:$databasePort",
      isConnectable = didManageToConnect,
      message = message
    )
  }
}