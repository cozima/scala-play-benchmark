package models

import com.fasterxml.uuid.Generators

case class RequestData(
  faces:    Seq[Seq[BigDecimal]],
  vertices: Seq[Seq[BigDecimal]],
  rings:    Seq[Seq[Seq[BigDecimal]]]
) {

  def toEntity: Entity = Entity(
    Generators.timeBasedGenerator().generate(),
    faces,
    vertices,
    rings
  )
}
