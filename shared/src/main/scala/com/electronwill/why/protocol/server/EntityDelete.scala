package com.electronwill
package why.protocol
package server

import niol._
import why.gametype.EntityId

case class EntityDelete(entityId: EntityId) extends ServerPacket(4) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId.id)
}
object EntityDelete extends PacketParser[EntityDelete](4) {
  def readData(in: NiolInput) =
    EntityDelete(EntityId(in.readUnsignedShort()))
}
