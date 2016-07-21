package com.lonelyplanet.scalahealthcheck

import org.zalando.jsonapi.model.RootObject.ResourceObject

sealed abstract class ServiceDependency(val healthChecker: HealthChecker, val `type`: String, val id: String) {
  def asResourceObject: ResourceObject = ResourceObject(
    `type` = `type`,
    id = Some(id)
  )
}

case class DatabaseServiceDependency(override val healthChecker: HealthChecker, override val id: String)
  extends ServiceDependency(healthChecker, "database-dependency-report", id)

case class WebServiceDependency(override val healthChecker: HealthChecker, override val id: String)
  extends ServiceDependency(healthChecker, "service-dependency-report", id)

