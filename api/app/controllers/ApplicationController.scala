package controllers

import java.util.{Locale, UUID}

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Decoder, parser}
import javax.inject.{Inject, Singleton}
import models.RequestData
import play.api.mvc._
import repositories.Repository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

@Singleton
class ApplicationController @Inject()(repository: Repository)(implicit ec: ExecutionContext)
  extends InjectedController
  with MessagepackSupport {

  def show(id: UUID): Action[AnyContent] = Action.async {
    repository.findById(id).map(e => Ok(msgpack(e.asJson)).as("application/x-msgpack"))
  }

  def showJson(id: UUID): Action[AnyContent] = Action.async {
    repository.findById(id).map(e => Ok(e.asJson.noSpaces).as("application/json"))
  }

  def create: Action[AnyContent] = Action.async { implicit request =>
    for {
      d <- decodeRequest[RequestData]
      _ <- repository.store(d.toEntity)
    } yield Created
  }

  private def decodeRequest[T: Decoder](implicit request: play.api.mvc.Request[_]): Future[T] = Future.fromTry {
    val contentType: Option[String] = request.contentType.map(_.toLowerCase(Locale.ENGLISH))
    request.body match {
      case AnyContentAsRaw(bytes) if contentType.contains("application/x-msgpack") =>
        bytes.asBytes().toRight(new Exception).flatMap(b => fromMessagepack[T](b.toArray)).toTry
      case AnyContentAsJson(json) => parser.decode[T](json.toString()).toTry
      case _ => Failure(new Exception)
    }
  }
}
