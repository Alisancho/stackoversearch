package ru.stackoversearch.service.stackoverflow

import sttp.model.{QueryParams, Uri}
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import ru.stackoversearch.entity.server.SearchResponse
import ru.stackoversearch.entity.stackoverflow.SofJson
import ru.stackoversearch.service.http.HttpClient
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import ru.stackoversearch.service.http.HttpClient.FResponse
import spray.json.JsonParser

import scala.concurrent.{ExecutionContext, Future}

object StackoverflowServiceImpl {

  val SEARCH_TAG = "tag"

  val searchParam = Map(
    "pagesize" -> "100",
    "order"    -> "desc",
    "sort"     -> "creation",
    "site"     -> "stackoverflow"
  )

}

class StackoverflowServiceImpl(mainUri: Uri, maxRequest: Int)(httpClient: HttpClient)(implicit
    m: Materializer,
    ex: ExecutionContext
) extends LazyLogging {

  import StackoverflowServiceImpl._

  def startSearch(tagsForSearch: Seq[(String, String)]): Future[Map[String, SearchResponse]] = {

    val tagsForSearchClean: Seq[(String, String)] = tagsForSearch.filter(_._1 == SEARCH_TAG)

    Source(tagsForSearchClean)
      .map(q => (q._2, mainUri.withParams(QueryParams.fromMap(searchParam.concat(Map("tagged" -> q._2))))))
      .wireTap(c => logger.info(c.toString))
      .mapAsync(maxRequest)(q => requestFunction(q._1, httpClient.sendGetRequest(q._2)))
      .filter(_.isRight)
      .map(i => i.toOption.get)
      .runWith(Sink.seq)
      .map(_.toMap)
  }

  private val requestFunction: (String, FResponse) => Future[Either[Throwable, (String, SearchResponse)]] =
    (tag, future) =>
      (for {
        resp  <- future
        body   = resp.body.map(JsonParser(_).convertTo[SofJson]).getOrElse(throw new RuntimeException("The body is not valid"))
        search = SearchResponse(body.items.size, body.items.count(_.is_answered == true))
      } yield (tag, search).asRight).recover(q => {
        logger.error(q.getMessage)
        q.asLeft
      })
}
