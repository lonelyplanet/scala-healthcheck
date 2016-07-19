package com.lonelyplanet.scalahealthcheck.jsonapi

import com.lonelyplanet.scalahealthcheck.util.OptionConversions
import com.lonelyplanet.scalahealthcheck.{DatabaseDependency, ServiceConfig, ServiceDependency}
import org.zalando.jsonapi.JsonapiRootObjectWriter
import org.zalando.jsonapi.model.JsonApiObject.{JsObjectValue, StringValue}
import org.zalando.jsonapi.model.RootObject.{ResourceObject, ResourceObjects}
import org.zalando.jsonapi.model._

import scala.collection.immutable.Seq

case class HealthCheckEntity(
  serviceConfig: ServiceConfig,
  serviceDependencies: Seq[ServiceDependency],
  maybeDatabaseDependency: Option[DatabaseDependency]
)

object HealthCheckEntity {
  private val HealthCheckType = "service"

  implicit val healthCheckJsonapiFormat =
    new JsonapiRootObjectWriter[HealthCheckEntity] {
      override def toJsonapi(h: HealthCheckEntity) = {
        RootObject(
          data = Some(
            ResourceObject(
              `type` = HealthCheckType,
              id = Some(h.serviceConfig.serviceInfo.name),
              attributes = attributes(h.serviceConfig),
              relationships = relationships(h.serviceDependencies)
            )
          ),
          included = included(h.maybeDatabaseDependency)
        )
      }

    }
  private def relationships(serviceDependencies: Seq[ServiceDependency]): Option[Map[String, Relationship]] = {
    for {
      serviceDependencies <- OptionConversions.seqToOption(serviceDependencies)
    } yield {
      Map(
        "dependencies" ->
          Relationship(
            data = Some(ResourceObjects(serviceDependencies.map(
              ServiceDependency.asResourceObject
            )))
          )
      )
    }
  }

  private def attributes(serviceConfig: ServiceConfig): Option[Attributes] = {
    Some(
      List(
        Attribute(
          "service-group-id",
          StringValue(serviceConfig.serviceInfo.groupId)
        ),
        Attribute(
          "service-id",
          StringValue(serviceConfig.serviceInfo.name)
        ),
        Attribute(
          "contact-info",
          JsObjectValue(
            List(
              Attribute(
                "service-owner-slackid",
                StringValue(
                  serviceConfig.contactInfo.serviceOwnerSlackId
                )
              ),
              Attribute(
                "slack-channel",
                StringValue(
                  serviceConfig.contactInfo.slackChannel
                )
              )
            )
          )
        ),
        Attribute(
          "github-repo-name",
          StringValue(serviceConfig.buildInfo.githubRepoName)
        ),
        Attribute(
          "github-commit",
          StringValue(serviceConfig.buildInfo.githubCommit)
        ),
        Attribute(
          "docker-image",
          StringValue(serviceConfig.buildInfo.dockerImage)
        )
      )
    )
  }

  private def included(maybeDatabaseDependency: Option[DatabaseDependency]): Option[Included] = {
    for {
      databaseDependency <- maybeDatabaseDependency
    } yield {
      Included(
        ResourceObjects(
          List(
            DatabaseDependency.asResourceObject(databaseDependency)
          )
        )
      )
    }
  }
}
