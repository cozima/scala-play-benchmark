package controllers

import io.circe.{Decoder, Json}
import msgpack4z.{CirceMsgpack, CirceUnpackOptions, MsgInBuffer, MsgOutBuffer}
import scalaz.{-\/, \/-}

trait MessagepackSupport {

  private val codec = CirceMsgpack.jsonCodec(CirceUnpackOptions.default)

  def fromMessagepack[A](data: Array[Byte])(implicit decoder: Decoder[A]): Either[Throwable, A] = {
    val unpacker = MsgInBuffer.apply(data)
    codec.unpack(unpacker) match {
      case \/-(result) => result.as[A]
      case -\/(e) => Left(e)
    }
  }

  def msgpack(r: Json): Array[Byte] = {
    codec.toBytes(r, MsgOutBuffer.create())
  }
}
