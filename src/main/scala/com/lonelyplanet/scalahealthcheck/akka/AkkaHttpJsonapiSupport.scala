package com.lonelyplanet.scalahealthcheck.akka

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import org.zalando.jsonapi.json.sprayjson.SprayJsonJsonapiProtocol
import org.zalando.jsonapi.model.RootObject
import spray.json._
import akka.http.scaladsl.model.MediaTypes.`application/vnd.api+json`
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import org.zalando.jsonapi._

trait AkkaHttpJsonapiSupport extends SprayJsonJsonapiProtocol with DefaultJsonProtocol {
  def akkaHttpJsonapiMarshaller[T: JsonapiRootObjectWriter]: ToEntityMarshaller[T] =
    Marshaller.StringMarshaller.wrap(`application/vnd.api+json`)(_.rootObject.toJson.compactPrint)

  def akkaHttpJsonapiUnmarshaller[T: JsonapiRootObjectReader]: FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller.forContentTypes(`application/vnd.api+json`).map(_.parseJson.convertTo[RootObject].jsonapi[T])
}

object AkkaHttpJsonapiSupport extends AkkaHttpJsonapiSupport {
  implicit def akkaHttpJsonapiMarshallerImplicit[T: JsonapiRootObjectWriter]: ToEntityMarshaller[T] = akkaHttpJsonapiMarshaller
  implicit def akkaHttpJsonapiUnmarshallerImplicit[T: JsonapiRootObjectReader]: FromEntityUnmarshaller[T] = akkaHttpJsonapiUnmarshaller
}
