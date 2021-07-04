package ru.stackoversearch.entity.server

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class SearchResponse(total: Int, answered: Int)

object SearchResponse extends DefaultJsonProtocol {
  implicit lazy val searchResponseJson: RootJsonFormat[SearchResponse] = jsonFormat2(SearchResponse.apply)
}
