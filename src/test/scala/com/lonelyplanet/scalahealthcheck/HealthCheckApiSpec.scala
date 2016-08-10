package com.lonelyplanet.scalahealthcheck

import org.scalatest.{FlatSpec, Matchers}
import spray.json._
import _root_.akka.http.scaladsl.testkit.ScalatestRouteTest
import com.lonelyplanet.scalahealthcheck.akka.http.routes.HealthCheckApiRoutes

import scala.collection.immutable.Seq
import _root_.akka.http.scaladsl.model.StatusCodes.OK
import org.zalando.jsonapi.json.akka.http.AkkaHttpJsonapiSupport._
import com.lonelyplanet.scalahealthcheck.fixtures.TestResponses
import com.lonelyplanet.scalahealthcheck.util.{AlwaysGreenHealthCheck, AlwaysRedHealthCheck, MockDatabaseHealthChecker}

class HealthCheckApiSpec extends BaseSpec {
  val databaseHealthCheck = new MockDatabaseHealthChecker

  val api = new HealthCheckApiRoutes(
    Seq(DatabaseServiceDependency(databaseHealthCheck, "green-db")),
    "health-check",
    alwaysRespondWithOK = true
  )

  "HealthCheck root object" should "should contain data" in {
    Get("/health-check") ~> api.routes ~> check {
      status shouldBe OK

      val json = responseAs[String].parseJson.asJsObject

      checkThatFieldNameExistsAndReturnIt("data", json.fields)
    }
  }

  "HealthCheck data" should "contain all known health check attributes" in {
    Get("/health-check") ~> api.routes ~> check {
      status shouldBe OK

      val json = responseAs[String].parseJson.asJsObject

      val data = checkThatFieldNameExistsAndReturnIt("data", json.fields).asJsObject
      val attributes = checkThatAttributesExistsAndReturnThem(data)

      attributes.size shouldBe 6

      checkThatFieldNameExistsAndReturnIt("lp-service-group-id", attributes)
      checkThatFieldNameExistsAndReturnIt("lp-service-id", attributes)
      checkThatFieldNameExistsAndReturnIt("contact-info", attributes)
      checkThatFieldNameExistsAndReturnIt("github-repo-name", attributes)
      checkThatFieldNameExistsAndReturnIt("github-commit", attributes)
      checkThatFieldNameExistsAndReturnIt("docker-image", attributes)
    }
  }

  "Database HealthCheck status" should "be green if database is connectable" in {
    setDatabaseConnectable(true)

    Get("/health-check?include=dependencies") ~> api.routes ~> check {
      status shouldBe OK

      val json = responseAs[String].parseJson.asJsObject

      val included = checkThatFieldNameExistsAndReturnIt("included", json.fields).convertTo[Seq[JsObject]].head
      val attributes = checkThatAttributesExistsAndReturnThem(included)
      val databaseStatus = checkThatFieldNameExistsAndReturnIt("status", attributes)

      databaseStatus shouldBe JsString("green")
    }
  }

  it should "be false if database is not connectable" in {
    setDatabaseConnectable(false)

    Get("/health-check?include=dependencies") ~> api.routes ~> check {
      status shouldBe OK

      val json = responseAs[String].parseJson.asJsObject

      val included = checkThatFieldNameExistsAndReturnIt("included", json.fields).convertTo[Seq[JsObject]].head
      val attributes = checkThatAttributesExistsAndReturnThem(included)
      val databaseStatus = checkThatFieldNameExistsAndReturnIt("status", attributes)

      databaseStatus shouldBe JsString("red")
    }
  }

  it should "return single health check and responses should match expected value" in {
    val healthCheckApi = new HealthCheckApiRoutes(
      Seq(DatabaseServiceDependency(new AlwaysGreenHealthCheck, "green-db")),
      "health-check",
      alwaysRespondWithOK = true
    )

    Get("/health-check") ~> healthCheckApi.routes ~> check {
      status shouldBe OK
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.singleHealthCheckDependencies
    }

    Get("/health-check?include=dependencies") ~> healthCheckApi.routes ~> check {
      status shouldBe OK
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.singleHealthCheckWithDependencies
    }
  }

  it should "return multiple health checks and responses should match expected value" in {
    val healthCheckApi = new HealthCheckApiRoutes(
      Seq(
        DatabaseServiceDependency(new AlwaysGreenHealthCheck, "green-db"),
        DatabaseServiceDependency(new AlwaysRedHealthCheck, "red-db")
      ),
      "health-check",
      alwaysRespondWithOK = true
    )

    Get("/health-check") ~> healthCheckApi.routes ~> check {
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.multipleHealthChecksNoDependencies
      status shouldBe OK
    }

    Get("/health-check?include=dependencies") ~> healthCheckApi.routes ~> check {
      val json = responseAs[String].parseJson
      json shouldBe TestResponses.multipleHealthChecksWithDependencies
      status shouldBe OK
    }
  }

  private def checkThatAttributesExistsAndReturnThem(jsObject: JsObject) = {
    val maybeAttributes = jsObject.fields.get("attributes")
    maybeAttributes.isDefined shouldBe true

    maybeAttributes.get.convertTo[Map[String, JsValue]]
  }

  private def checkThatFieldNameExistsAndReturnIt(fieldName: String, data: Map[String, JsValue]) = {
    val maybeField = data.get(fieldName)
    maybeField.isDefined shouldBe true

    maybeField.get
  }

  private def setDatabaseConnectable(connectable: Boolean) = {
    databaseHealthCheck.connectable = connectable
  }

}