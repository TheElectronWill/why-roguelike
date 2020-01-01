package com.electronwill
package why.protocol
package server

import niol._
import why.geom.Vec2i
import why.gametype.EntityId

case class EntityMove(entityId: EntityId, newPosition: Vec2i) extends ServerPacket(5) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId.id)
    out.writeVector(newPosition)
}
object EntityMove extends PacketParser[EntityMove](5) {
  def readData(in: NiolInput) =
    EntityMove(EntityId(in.readUnsignedShort()), in.readVector())
}
