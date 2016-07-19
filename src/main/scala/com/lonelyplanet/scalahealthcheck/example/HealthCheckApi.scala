package com.lonelyplanet.scalahealthcheck.example

import akka.actor.ActorSystem
import com.lonelyplanet.scalahealthcheck._
import com.lonelyplanet.scalahealthcheck.jsonapi.HealthCheckEntity
import com.typesafe.config.ConfigFactory
import _root_.akka.http.scaladsl.server.Directives._
import akka.AkkaHttpJsonapiSupport._
import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContextExecutor

class HealthCheckApi(implicit val system: ActorSystem, implicit val executor: ExecutionContextExecutor) {
  val dbHealthCheck = new DatabaseHealthCheckerImpl("localhost", 3306)

  val routes = {
    get {
      path("health-check") {
        parameters("include".?) { maybeInclude =>
          complete {
            val dbServiceDependency =
              ServiceDependency.ArticlesDBServiceDependency
            val maybeDbDependency = for {
              include <- maybeInclude if include == "dependencies"
            } yield {
              DatabaseDependency(dbServiceDependency, dbHealthCheck.check)
            }

            HealthCheckEntity(
              ServiceConfig(ConfigFactory.load()),
              Seq(dbServiceDependency),
              maybeDbDependency
            )
          }
        }
      }
    }
  }
}

class DatabaseHealthCheckerImpl(serverName: String, port: Int) extends DatabaseHealthChecker {
  def check: DatabaseHealth = DatabaseHealth(serverName, port, isConnectable = true) // TODO
}
