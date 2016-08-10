package com.lonelyplanet.scalahealthcheck

import _root_.akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}

class HealthCheckWithOptionToChangeStatusCodeApiSpec extends BaseSpec {

  it should "not set status code to 500 if no dependencies are requested" in {
    Get("/health-check") ~> redApi.routes ~> check {
      status shouldBe OK
    }
  }

  it should "set status code to 500 if dependencies are requested" in {
    Get("/health-check?include=dependencies") ~> redApi.routes ~> check {
      status shouldBe InternalServerError
    }
  }

  it should "set status code to 200 if everything is OK" in {
    Get("/health-check?include=dependencies") ~> greenApi.routes ~> check {
      status shouldBe OK
    }
  }

}