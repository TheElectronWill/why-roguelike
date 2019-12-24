package com.electronwill
package why.protocol

import niol._
import why.Vec2i
import server.ServerPacket
import client.ClientPacket

case class Warning(message: String) extends ServerPacket(254) with ClientPacket {
  def writeData(out: NiolOutput): Unit = out.writeVarString(message)
}
object Warning extends PacketParser[Warning](254) {
  def readData(in: NiolInput) = Warning(in.readVarString())
}
