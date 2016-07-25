package com.lonelyplanet.scalahealthcheck.jsonapi

import com.lonelyplanet.scalahealthcheck.{DependencyDetails, ServiceConfig, ServiceDependency}
import com.lonelyplanet.util.OptionConversions._
import org.zalando.jsonapi.JsonapiRootObjectWriter
import org.zalando.jsonapi.model.JsonApiObject.{JsObjectValue, StringValue}
import org.zalando.jsonapi.model.RootObject.{ResourceObject, ResourceObjects}
import org.zalando.jsonapi.model._

import scala.collection.immutable.Seq

case class HealthCheckEntity(
  serviceConfig: ServiceConfig,
  serviceDependencies: Seq[ServiceDependency],
  dependencyDetails: Seq[DependencyDetails]
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
          included = included(h.dependencyDetails)
        )
      }
    }

  private def relationships(serviceDependencies: Seq[ServiceDependency]): Option[Map[String, Relationship]] = {
    for {
      serviceDependencies <- seqToOption(serviceDependencies)
    } yield {
      Map(
        "dependencies" ->
          Relationship(
            data = Some(ResourceObjects(serviceDependencies.map(
              _.asResourceObject
            )))
          )
      )
    }
  }

  private def attributes(serviceConfig: ServiceConfig): Option[Attributes] = {
    Some(
      List(
        Attribute(
          "lp-service-group-id",
          StringValue(serviceConfig.serviceInfo.groupId)
        ),
        Attribute(
          "lp-service-id",
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

  private def included(dependencies: Seq[DependencyDetails]): Option[Included] = {
    wrapOption(
      dependencies.nonEmpty,
      Included(
        ResourceObjects(
          dependencies.map(DependencyDetails.asResourceObject)
        )
      )
    )
  }
}
