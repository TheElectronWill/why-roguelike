package com.electronwill
package why.protocol

import niol.{NiolInput, NiolOutput}
import scala.util.{Try, Success, Failure}
import scala.reflect.ClassTag

abstract class PacketRegistry[P <: Packet] {
  private val maxId = 255
  private val parsers = new Array[PacketParser[P]](maxId + 1)

  def register(p: PacketParser[P]): Unit =
    parsers(p.id) = p

  def get(id: Int) = parsers(id)

  def parse(in: NiolInput): Try[P] =
    val id = in.readUnsignedByte()
    val parser = parsers(id)
    if parser == null
      Failure(UnknownPacketException(id, getClass.getSimpleName))
    else
      Try(parser.readData(in))

  def write(p: P, out: NiolOutput): Unit =
    out.writeByte(p.id & 0xff)
    p.writeData(out)

}
