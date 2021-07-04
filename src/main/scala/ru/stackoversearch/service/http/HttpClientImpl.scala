package ru.stackoversearch.service.http

import akka.actor.ActorSystem
import ru.stackoversearch.service.http.HttpClient.FResponse
import sttp.client3.akkahttp.AkkaHttpBackend
import sttp.client3.basicRequest
import sttp.model.Uri

class HttpClientImpl(implicit system: ActorSystem) extends HttpClient {
  private val backend = AkkaHttpBackend.usingActorSystem(system)

  override def sendGetRequest(request: Uri): FResponse =
    basicRequest
      .get(request)
      .send(backend)
}
