package com.electronwill
package why.protocol
package server

import niol._
import why.geom.Vec2i

case class TileUpdate(position: Vec2i, newTypeId: Int) extends ServerPacket(7) {
  def writeData(out: NiolOutput): Unit =
    out.writeVector(position)
    out.writeVarInt(newTypeId)
}
object TileUpdate extends PacketParser[TileUpdate](7) {
  def readData(in: NiolInput) =
    TileUpdate(in.readVector(), in.readVarInt())
}
