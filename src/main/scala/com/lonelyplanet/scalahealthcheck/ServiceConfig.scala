package com.lonelyplanet.scalahealthcheck

import com.typesafe.config.Config

case class ServiceConfig(serviceInfo: ServiceInfo, contactInfo: ContactInfo, buildInfo: BuildInfo)

object ServiceConfig {
  private val ServiceKey = "service"
  private val EnvironmentKey = "environment"
  private val NameKey = "lp-service-id"
  private val GroupIdKey = "lp-service-group-id"
  private val OwnerSlackIdKey = "owner-slack-id"
  private val SlackChannelKey = "slack-channel"
  private val GitHubCommitKey = "github-commit"
  private val GitHubRepoKey = "github-repo"
  private val DockerImageKey = "docker-image"

  def fromConfig(config: Config): ServiceConfig = {
    val service = config.getConfig(ServiceKey)
    val name = service.getString(NameKey)
    val environment = service.getString(EnvironmentKey)
    val groupId = service.getString(GroupIdKey)
    val ownerSlackId = service.getString(OwnerSlackIdKey)
    val slackChannel = service.getString(SlackChannelKey)
    val githubCommit = service.getString(GitHubCommitKey)
    val githubRepo = service.getString(GitHubRepoKey)
    val dockerImage = service.getString(DockerImageKey)

    ServiceConfig(
      serviceInfo = ServiceInfo(name, groupId, environment),
      contactInfo = ContactInfo(ownerSlackId, slackChannel),
      buildInfo = BuildInfo(githubCommit, githubRepo, dockerImage)
    )
  }
}

case class ContactInfo(serviceOwnerSlackId: String, slackChannel: String)
case class ServiceInfo(name: String, groupId: String, environment: String)
case class BuildInfo(githubCommit: String, githubRepoName: String, dockerImage: String)
