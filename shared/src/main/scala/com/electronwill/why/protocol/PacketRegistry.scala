package com.electronwill
package why.protocol

import niol.{NiolInput, NiolOutput}
import scala.util.{Try, Success, Failure}

abstract class PacketRegistry {
  private val maxId = 255
  private val parsers = new Array[PacketParser[_]](maxId+1)

  def register(p: PacketParser[_]): Unit =
    parsers(p.id) = p

  def get(id: Int) = parsers(id)

  def parse(in: NiolInput): Try[Packet] =
    val id = in.readUnsignedByte()
    val parser = parsers(id)
    if parser == null
      Failure(UnknownPacketException(id, "client->server"))
    else
      Try(parser.readData(in))

  def write(p: Packet, out: NiolOutput): Unit =
    out.writeByte(p.id)
    p.writeData(out)

  def init(): Unit
}
