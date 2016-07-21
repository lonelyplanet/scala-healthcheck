package com.lonelyplanet.scalahealthcheck

import org.zalando.jsonapi.model.Attribute
import org.zalando.jsonapi.model.JsonApiObject.StringValue
import org.zalando.jsonapi.model.RootObject.ResourceObject

case class DependencyDetails(`type`: String, id: String, location: String, status: DependencyStatus, description: String)

object DependencyDetails {
  def apply(databaseServiceDependency: ServiceDependency, databaseHealth: HealthCheckResult): DependencyDetails = {
    val status = if (databaseHealth.isConnectable) DependencyStatusGreen else DependencyStatusRed

    DependencyDetails(
      databaseServiceDependency.`type`,
      databaseServiceDependency.id,
      databaseHealth.location,
      status,
      databaseHealth.message
    )
  }

  def asResourceObject(o: DependencyDetails): ResourceObject =
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