package ru.stackoversearch

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import ru.stackoversearch.config.Config
import ru.stackoversearch.config.Config.Server.{SERVER_HOST, SERVER_PORT}
import ru.stackoversearch.config.Config.Stackoverflow.{MAX_REQUEST, SOF_URL}
import ru.stackoversearch.controller.http.Routs
import ru.stackoversearch.service.http.HttpClientImpl
import ru.stackoversearch.service.stackoverflow.StackoverflowServiceImpl

import scala.concurrent.ExecutionContextExecutor

object AppStart extends App {

  implicit val system: ActorSystem          = ActorSystem("mainActorSystem", Config.config)
  implicit val materializer: Materializer   = Materializer(system)
  implicit val ex: ExecutionContextExecutor = system.dispatcher

  val stackoverflowService = new StackoverflowServiceImpl(SOF_URL, MAX_REQUEST)(new HttpClientImpl())
  val routs                = new Routs(stackoverflowService).getRout

  Http()
    .newServerAt(SERVER_HOST, SERVER_PORT)
    .bind(routs)

}
