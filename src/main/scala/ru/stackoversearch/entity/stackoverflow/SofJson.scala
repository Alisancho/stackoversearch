package ru.stackoversearch.entity.stackoverflow

import spray.json.DefaultJsonProtocol
import spray.json._

case class SofJson(items: List[Items])
case class Items(is_answered: Boolean)

object SofJson extends DefaultJsonProtocol {
  import Items._
  implicit lazy val sofJson: RootJsonFormat[SofJson] = jsonFormat1(SofJson.apply)
}

object Items   extends DefaultJsonProtocol {
  implicit lazy val itemsFormat: RootJsonFormat[Items] = jsonFormat1(Items.apply)
}
