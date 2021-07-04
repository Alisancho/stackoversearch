package ru.stackoversearch.controller.http

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.{complete, extractUri, get, handleExceptions, parameters, path}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.http.scaladsl.server.Directives._
import ru.stackoversearch.service.stackoverflow.StackoverflowServiceImpl
import spray.json.DefaultJsonProtocol._
import spray.json.enrichAny

import scala.concurrent.ExecutionContext

class Routs(stackoverflowService: StackoverflowServiceImpl)(implicit ec: ExecutionContext) {
  def getRout: Route =
    path("search") {
      get {
        parameterSeq { p =>
          handleExceptions(myExceptionHandler) {
            complete(
              stackoverflowService
                .startSearch(p)
                .map(_.toJson.toString())
            )
          }
        }
      }
    }

  def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: Throwable =>
        extractUri { uri =>
          complete(HttpResponse(InternalServerError))
        }
    }
}
