package com.lonelyplanet.scalahealthcheck.akka.http.routes

import akka.actor.ActorSystem
import com.lonelyplanet.scalahealthcheck._
import com.lonelyplanet.scalahealthcheck.jsonapi.HealthCheckEntity
import com.typesafe.config.ConfigFactory
import _root_.akka.http.scaladsl.server.Directives._
import org.zalando.jsonapi.json.akka.http.AkkaHttpJsonapiSupport._
import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContextExecutor
import com.lonelyplanet.util.OptionConversions._
import _root_.akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}

class HealthCheckApiRoutes(dependencies: Seq[ServiceDependency], healthCheckEndpoint: String, alwaysRespondWithOK: Boolean)(implicit val system: ActorSystem, implicit val executor: ExecutionContextExecutor) {
  val routes = {
    get {
      path(healthCheckEndpoint) {
        parameters("include".?) { maybeInclude =>
          complete {

            val dependencyDetails = wrapOption(
              maybeInclude.contains("dependencies"),
              dependencies.map { d =>
                DependencyDetails(d, d.healthChecker.check)
              }
            ).getOrElse(Seq.empty)

            val healthCheck = HealthCheckEntity(
              ServiceConfig.fromConfig(ConfigFactory.load()),
              dependencies,
              dependencyDetails
            )

            (responseCode(dependencyDetails), healthCheck)
          }
        }
      }
    }
  }

  private def responseCode(dependencyDetails: Seq[DependencyDetails]) = {
    if (areThereAnyProblems(dependencyDetails) && !alwaysRespondWithOK) {
      InternalServerError
    } else {
      OK
    }
  }

  private def areThereAnyProblems(dependencyDetails: Seq[DependencyDetails]) = {
    dependencyDetails.map(_.status).contains(DependencyStatusRed)
  }
}