package com.lonelyplanet.scalahealthcheck

import org.zalando.jsonapi.model.Attribute
import org.zalando.jsonapi.model.JsonApiObject.StringValue
import org.zalando.jsonapi.model.RootObject.ResourceObject

sealed abstract class ServiceDependency(val `type`: String, val id: String)

case class DatabaseServiceDependency(override val id: String) extends ServiceDependency(ServiceDependency.DatabaseType, id)

object ServiceDependency {
  val DatabaseType = "database-dependency-report"
  val DatabaseId = "db-articles"
  val ArticlesDBServiceDependency = DatabaseServiceDependency(DatabaseId)

  def asResourceObject(o: ServiceDependency): ResourceObject = ResourceObject(
    `type` = o.`type`,
    id = Some(o.id)
  )
}

case class DatabaseDependency(
  `type`: String,
  id: String,
  location: String,
  status: DependencyStatus,
  description: String
)

object DatabaseDependency {
  def apply(databaseServiceDependency: DatabaseServiceDependency, databaseHealth: DatabaseHealth): DatabaseDependency = {
    val status = if (databaseHealth.isConnectable) DependencyStatusGreen else DependencyStatusRed

    DatabaseDependency(
      databaseServiceDependency.`type`,
      databaseServiceDependency.id,
      databaseHealth.location,
      status,
      databaseHealth.message
    )
  }

  def asResourceObject(o: DatabaseDependency): ResourceObject =
    ResourceObject(
      `type` = o.`type`,
      id = Some(o.id),
      attributes = Some(
        List(
          Attribute("location", StringValue(o.location)),
          Attribute("status", StringValue(o.status.value)),
          Attribute("description", StringValue(o.description))
        )
      )
    )
}

sealed abstract class DependencyStatus(val value: String)
case object DependencyStatusGreen extends DependencyStatus("green")
case object DependencyStatusRed extends DependencyStatus("red")

case class DatabaseHealth(
  location: String,
  isConnectable: Boolean,
  message: String
)
object DatabaseHealth {
  private val DatabaseConnectableMessage = "No problems found"
  private val DatabaseNotConnectableMessage = "Could not connect to database"

  def apply(databaseHost: String, databasePort: Int, isConnectable: Boolean): DatabaseHealth = {
    val message = if (isConnectable) DatabaseConnectableMessage else DatabaseNotConnectableMessage

    DatabaseHealth(
      location = s"$databaseHost:$databasePort",
      isConnectable = isConnectable,
      message = message
    )
  }
}
