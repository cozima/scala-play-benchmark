package models

import java.util.UUID

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

import scala.collection.JavaConverters._

case class Entity(
  id:       UUID,
  faces:    Seq[Seq[BigDecimal]],
  vertices: Seq[Seq[BigDecimal]],
  rings:    Seq[Seq[Seq[BigDecimal]]]
)

object Entity {

  def apply(value: Map[String, AttributeValue]): Entity =
    Entity(
      id       = UUID.fromString(value("id").s()),
      faces    = value("faces").l().asScala.map(_.l().asScala.map(n => BigDecimal(n.n()))),
      vertices = value("vertices").l().asScala.map(_.l().asScala.map(n => BigDecimal(n.n()))),
      rings    = value("rings").l().asScala.map(_.l().asScala.map(_.l().asScala.map(n => BigDecimal(n.n()))))
    )

}
