package akka_http.marshalling

import akka.http.scaladsl.marshalling.{Marshaller, Marshalling, ToEntityMarshaller}
import akka.http.scaladsl.model.ContentTypes

object Marshallers {
  implicit val toStringMarshaller: ToEntityMarshaller[Int] = Marshaller
    .strict((number: Int) => Marshalling.WithFixedContentType(ContentTypes.`text/plain(UTF-8)`, () => s"$number\n"))
}
