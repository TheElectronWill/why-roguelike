package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class AreaUpdate(from: Vec2i, to: Vec2i, tilesIds: Array[Int]) extends ServerPacket(9) {
  def writeData(out: NiolOutput): Unit =
    out.writeVector(from)
    out.writeVector(to)
    out.writeInts(tilesIds)
}
object AreaUpdate extends PacketParser[AreaUpdate](9) {
  def readData(in: NiolInput) =
    val from = in.readVector()
    val to = in.readVector()
    val count = to.x-from.x + to.y-from.y
    val tiles = new Array[Int](count)
    for i <- 1 to count do
      tiles(i) = in.readVarInt()
    AreaUpdate(from, to, tiles)
}
