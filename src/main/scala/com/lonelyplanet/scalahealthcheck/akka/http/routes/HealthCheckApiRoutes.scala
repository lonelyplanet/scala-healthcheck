package com.lonelyplanet.scalahealthcheck.akka.http.routes

import akka.actor.ActorSystem
import com.lonelyplanet.scalahealthcheck._
import com.lonelyplanet.scalahealthcheck.jsonapi.HealthCheckEntity
import com.typesafe.config.ConfigFactory
import _root_.akka.http.scaladsl.server.Directives._
import akka.AkkaHttpJsonapiSupport._
import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContextExecutor

class HealthCheckApiRoutes(healthChecker: DatabaseHealthChecker, healthCheckEndpoint: String)(implicit val system: ActorSystem, implicit val executor: ExecutionContextExecutor) {
  val routes = {
    get {
      path(healthCheckEndpoint) {
        parameters("include".?) { maybeInclude =>
          complete {
            val dbServiceDependency = DatabaseServiceDependency("db-id")
            val maybeDbDependency = for {
              include <- maybeInclude if include == "dependencies"
            } yield {
              DatabaseDependency(dbServiceDependency, healthChecker.check)
            }

            HealthCheckEntity(
              ServiceConfig.fromConfig(ConfigFactory.load()),
              Seq(dbServiceDependency),
              maybeDbDependency
            )
          }
        }
      }
    }
  }
}