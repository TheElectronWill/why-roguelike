package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class EntitySpawn(entityId: Int, typeId: Int, position: Vec2i) extends Packet(3) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId)
    out.writeVarInt(typeId)
    out.writeVector(position)
}
object EntitySpawn extends PacketParser[EntitySpawn](3) {
  def readData(in: NiolInput) =
    EntitySpawn(in.readUnsignedShort(), in.readVarInt(), in.readVector())
}
