package ru.stackoversearch.service.http
import ru.stackoversearch.service.http.HttpClient.FResponse
import sttp.model.Uri
import sttp.client3.Response

import scala.concurrent.Future

object HttpClient {
  type FResponse = Future[Response[Either[String, String]]]
}
trait HttpClient  {
  def sendGetRequest(request: Uri): FResponse
}
