package services

import io.circe.{Json, JsonObject}
import javax.inject.Singleton
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.{AttributeValue, GetItemRequest, PutItemRequest, PutItemResponse}

import scala.collection.JavaConverters._
import scala.compat.java8.FutureConverters._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DynamodbService {

  def getItem(tableName: String, key: String)(implicit ec: ExecutionContext): Future[Map[String, AttributeValue]] = {
    val value   = AttributeValue.builder().s(key).build()
    val request = GetItemRequest.builder().tableName(tableName).key(Map("id" -> value).asJava).build()
    client.getItem(request).toScala.map(_.item().asScala.toMap)
  }

  def putItem(tableName: String, value: Map[String, AttributeValue]): Future[PutItemResponse] = {
    val request = PutItemRequest
      .builder()
      .tableName(tableName)
      .conditionExpression("attribute_not_exists(id)")
      .item(value.asJava)
      .build()
    client.putItem(request).toScala
  }

  private lazy val client: DynamoDbAsyncClient = {
    val region: Region = Region.of("eu-west-1")
    DynamoDbAsyncClient.builder().region(region).build()
  }

  def fromJson(js: JsonObject): Map[String, AttributeValue] =
    js.toMap.mapValues(convert).foldLeft[Map[String, AttributeValue]](Map.empty)(_ ++ Map(_))

  private def convert(js: Json): AttributeValue =
    if (js.isArray) {
      js.asArray.map(v => AttributeValue.builder().l(v.map(convert): _*).build()).get
    } else if (js.isBoolean) {
      js.asBoolean.map(v => AttributeValue.builder().bool(v).build()).get
    } else if (js.isNumber) {
      js.asNumber.map(v => AttributeValue.builder().n(v.toString).build()).get
    } else if (js.isString) {
      js.asString.map(v => AttributeValue.builder().s(v).build()).get
    } else {
      AttributeValue.builder().nul(true).build()
    }

}
