package com.lonelyplanet.scalahealthcheck

case class DatabaseHealth(location: String, isConnectable: Boolean, message: String)

object DatabaseHealth {
  private val DatabaseConnectableMessage = "No problems found"
  private val DatabaseNotConnectableMessage = "Could not connect to database"

  def apply(databaseHost: String, databasePort: Int, isConnectable: => Boolean): DatabaseHealth = {
    val didManageToConnect = isConnectable
    val message = if (didManageToConnect) DatabaseConnectableMessage else DatabaseNotConnectableMessage

    DatabaseHealth(
      location = s"$databaseHost:$databasePort",
      isConnectable = didManageToConnect,
      message = message
    )
  }
}