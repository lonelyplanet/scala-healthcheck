package com.lonelyplanet.scalahealthcheck.akka

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import org.zalando.jsonapi.json.sprayjson.SprayJsonJsonapiFormat
import org.zalando.jsonapi.model.RootObject
import spray.json._
import akka.http.scaladsl.model.MediaTypes.`application/vnd.api+json`
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import org.zalando.jsonapi._

trait AkkaHttpJsonapiSupport
    extends SprayJsonJsonapiFormat
    with DefaultJsonProtocol {
  implicit def akkaHttpJsonJsonapiMarshaller[T: JsonapiRootObjectWriter]
    : ToEntityMarshaller[T] =
    Marshaller.withFixedContentType(`application/vnd.api+json`)(
        _.rootObject.toJson.compactPrint)

  implicit def akkaHttpJsonJsonapiUnmarshaller[T: JsonapiRootObjectReader]
    : FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller
      .forContentTypes(`application/vnd.api+json`)
      .map(_.parseJson.convertTo[RootObject].jsonapi[T])
}

object AkkaHttpJsonapiSupport extends AkkaHttpJsonapiSupport
