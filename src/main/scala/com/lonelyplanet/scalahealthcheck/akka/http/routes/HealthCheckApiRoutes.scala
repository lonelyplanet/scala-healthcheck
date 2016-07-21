package com.lonelyplanet.scalahealthcheck.akka.http.routes

import akka.actor.ActorSystem
import com.lonelyplanet.scalahealthcheck._
import com.lonelyplanet.scalahealthcheck.jsonapi.HealthCheckEntity
import com.typesafe.config.ConfigFactory
import _root_.akka.http.scaladsl.server.Directives._
import akka.AkkaHttpJsonapiSupport._
import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContextExecutor
import com.lonelyplanet.util.OptionConversions._

class HealthCheckApiRoutes(dependencies: Seq[ServiceDependency], healthCheckEndpoint: String)(implicit val system: ActorSystem, implicit val executor: ExecutionContextExecutor) {
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

            HealthCheckEntity(
              ServiceConfig.fromConfig(ConfigFactory.load()),
              dependencies,
              dependencyDetails
            )
          }
        }
      }
    }
  }
}