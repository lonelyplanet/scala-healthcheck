# scala-healthcheck

[![Join the chat at https://gitter.im/lonelyplanet/scala-healthcheck](https://badges.gitter.im/lonelyplanet/scala-healthcheck.svg)](https://gitter.im/lonelyplanet/scala-healthcheck?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/lonelyplanet/scala-healthcheck.svg?branch=master)](https://travis-ci.org/lonelyplanet/scala-healthcheck)
[![codecov](https://codecov.io/gh/lonelyplanet/scala-healthcheck/branch/master/graph/badge.svg)](https://codecov.io/gh/lonelyplanet/scala-healthcheck)
[ ![Download](https://api.bintray.com/packages/lonelyplanet/maven/scala-healthcheck/images/download.svg) ](https://bintray.com/lonelyplanet/maven/scala-healthcheck/_latestVersion)

### Downloading

    "com.lonelyplanet" %% "scala-healthcheck" % "0.2.7"

You might need to also add our repository:

```
resolvers ++= Seq(
  Resolver.bintrayRepo("lonelyplanet", "maven")
)
```    

### Description

This is a simple library to provide a health checking capabilities for your Scala projects

At the moment this project is in the early experimental state so it will change a lot

Modules:
* Core
* JsonAPI
* Akka-http

### Changelog

0.2
- Improved customization  
- Added option to return HTTP error codes in case Health check fails
- Improved testing

0.1
- First public release

### Publishing

We use `bintray-sbt` plugin for publishing artifacts, to publish newer version of the library run:
```
sbt publish
```

That's all what is required
