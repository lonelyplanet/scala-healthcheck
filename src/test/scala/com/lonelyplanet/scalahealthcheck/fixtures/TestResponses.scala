package com.lonelyplanet.scalahealthcheck.fixtures
import spray.json._

object TestResponses {
  val singleHealthCheckDependencies =
    """
      {
      |  "data": {
      |    "type": "op-service",
      |    "id": "scala-healthcheck",
      |    "attributes": {
      |      "contact-info": {
      |        "service-owner-slackid": "@wlk",
      |        "slack-channel": "#testing"
      |      },
      |      "lp-service-group-id": "scala",
      |      "github-commit": "unknown",
      |      "lp-service-id": "scala-healthcheck",
      |      "github-repo-name": "scala-healthcheck",
      |      "docker-image": "unknown"
      |    },
      |    "relationships": {
      |      "dependencies": {
      |        "data": [
      |          {
      |            "type": "database-dependency-report",
      |            "id": "green-db"
      |          }
      |        ]
      |      }
      |    }
      |  }
      |}
    """.stripMargin.parseJson

  val singleHealthCheckWithDependencies =
    """
      {
      |  "data": {
      |    "type": "op-service",
      |    "id": "scala-healthcheck",
      |    "attributes": {
      |      "contact-info": {
      |        "service-owner-slackid": "@wlk",
      |        "slack-channel": "#testing"
      |      },
      |      "lp-service-group-id": "scala",
      |      "github-commit": "unknown",
      |      "lp-service-id": "scala-healthcheck",
      |      "github-repo-name": "scala-healthcheck",
      |      "docker-image": "unknown"
      |    },
      |    "relationships": {
      |      "dependencies": {
      |        "data": [
      |          {
      |            "type": "database-dependency-report",
      |            "id": "green-db"
      |          }
      |        ]
      |      }
      |    }
      |  },
      |  "included": [
      |    {
      |      "type": "database-dependency-report",
      |      "id": "green-db",
      |      "attributes": {
      |        "location": "host:3306",
      |        "status": "green",
      |        "description": "No problems found"
      |      }
      |    }
      |  ]
      |}
    """.stripMargin.parseJson

  val multipleHealthChecksNoDependencies =
    """
      |{
      |  "data": {
      |    "type": "op-service",
      |    "id": "scala-healthcheck",
      |    "attributes": {
      |      "contact-info": {
      |        "service-owner-slackid": "@wlk",
      |        "slack-channel": "#testing"
      |      },
      |      "lp-service-group-id": "scala",
      |      "github-commit": "unknown",
      |      "lp-service-id": "scala-healthcheck",
      |      "github-repo-name": "scala-healthcheck",
      |      "docker-image": "unknown"
      |    },
      |    "relationships": {
      |      "dependencies": {
      |        "data": [
      |          {
      |            "type": "database-dependency-report",
      |            "id": "green-db"
      |          },
      |          {
      |            "type": "database-dependency-report",
      |            "id": "red-db"
      |          }
      |        ]
      |      }
      |    }
      |  }
      |}
    """.stripMargin.parseJson
  val multipleHealthChecksWithDependencies =
    """
      {
      |  "data": {
      |    "type": "op-service",
      |    "id": "scala-healthcheck",
      |    "attributes": {
      |      "contact-info": {
      |        "service-owner-slackid": "@wlk",
      |        "slack-channel": "#testing"
      |      },
      |      "lp-service-group-id": "scala",
      |      "github-commit": "unknown",
      |      "lp-service-id": "scala-healthcheck",
      |      "github-repo-name": "scala-healthcheck",
      |      "docker-image": "unknown"
      |    },
      |    "relationships": {
      |      "dependencies": {
      |        "data": [
      |          {
      |            "type": "database-dependency-report",
      |            "id": "green-db"
      |          },
      |          {
      |            "type": "database-dependency-report",
      |            "id": "red-db"
      |          }
      |        ]
      |      }
      |    }
      |  },
      |  "included": [
      |    {
      |      "type": "database-dependency-report",
      |      "id": "green-db",
      |      "attributes": {
      |        "location": "host:3306",
      |        "status": "green",
      |        "description": "No problems found"
      |      }
      |    },
      |    {
      |      "type": "database-dependency-report",
      |      "id": "red-db",
      |      "attributes": {
      |        "location": "host:3306",
      |        "status": "red",
      |        "description": "Could not connect to database"
      |      }
      |    }
      |  ]
      |}
    """.stripMargin.parseJson
}
