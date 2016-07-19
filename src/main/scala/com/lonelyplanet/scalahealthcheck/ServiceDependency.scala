package com.lonelyplanet.scalahealthcheck

import org.zalando.jsonapi.model.RootObject.ResourceObject

sealed abstract class ServiceDependency(val `type`: String, val id: String)

case class DatabaseServiceDependency(override val id: String) extends ServiceDependency(ServiceDependency.DatabaseType, id)

object ServiceDependency {
  val DatabaseType = "database-dependency-report"
  val DatabaseId = "db-id"
  val DBServiceDependency = DatabaseServiceDependency(DatabaseId)

  def asResourceObject(o: ServiceDependency): ResourceObject = ResourceObject(
    `type` = o.`type`,
    id = Some(o.id)
  )
}
