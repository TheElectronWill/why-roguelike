package com.electronwill
package why.protocol
package server

import niol._

case class EntityDelete(entityId: Int) extends ServerPacket(4) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId)
}
object EntityDelete extends PacketParser[EntityDelete](4) {
  def readData(in: NiolInput) =
    EntityDelete(in.readUnsignedShort())
}
