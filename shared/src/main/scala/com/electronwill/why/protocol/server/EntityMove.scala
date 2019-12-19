package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class EntityMove(entityId: Int, newPosition: Vec2i) extends Packet(5) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId)
    out.writeVector(newPosition)
}
object EntityMove extends PacketParser[EntityMove](5) {
  def readData(in: NiolInput) =
    EntityMove(in.readUnsignedShort(), in.readVector())
}
