package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class Warning(message: String) extends ServerPacket(254) {
  def writeData(out: NiolOutput): Unit = out.writeVarString(message)
}
object Warning extends PacketParser[Warning](254) {
  def readData(in: NiolInput) = Warning(in.readVarString())
}
