package com.lonelyplanet.scalahealthcheck

import org.zalando.jsonapi.model.RootObject.ResourceObject

sealed abstract class ServiceDependency(val `type`: String, val id: String) {
  def asResourceObject: ResourceObject = ResourceObject(
    `type` = `type`,
    id = Some(id)
  )
}

case class DatabaseServiceDependency(override val id: String) extends ServiceDependency("database-dependency-report", id)

case class WebServiceDependency(override val id: String) extends ServiceDependency("service-dependency-report", id)

