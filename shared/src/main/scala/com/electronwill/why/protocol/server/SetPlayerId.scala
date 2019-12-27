package com.electronwill
package why.protocol
package server

import niol._
import why.Vec2i
import why.gametype.EntityId

case class SetPlayerId(entityId: EntityId) extends ServerPacket(8) {
  def writeData(out: NiolOutput): Unit = out.writeShort(entityId.id)
}
object SetPlayerId extends PacketParser[SetPlayerId](8) {
  def readData(in: NiolInput) = SetPlayerId(EntityId(in.readUnsignedShort()))
}
