package repositories

import java.util.UUID

import io.circe.generic.auto._
import io.circe.syntax._
import javax.inject.{Inject, Singleton}
import models.Entity
import services.DynamodbService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class Repository @Inject()(dynamodbService: DynamodbService) {

  private val tableName: String = "measurements_test"

  def findById(id: UUID)(implicit ec: ExecutionContext): Future[Entity] =
    dynamodbService.getItem(tableName, id.toString).map(Entity(_))

  def store(entity: Entity)(implicit ec: ExecutionContext): Future[Unit] =
    dynamodbService.putItem(tableName, dynamodbService.fromJson(entity.asJsonObject)).map(_ => ())

}
