package com.electronwill
package why.protocol
package server

import niol._
import why.geom.Vec2i

case class TerrainData(levelId: Int, levelName: String, width: Int, height: Int, spawn: Vec2i, exit: Vec2i, tilesIds: Array[Int]) extends ServerPacket(2) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(levelId)
    out.writeVarString(levelName)
    out.writeShort(width)
    out.writeShort(height)
    out.writeVector(spawn)
    out.writeVector(exit)
    for t <- tilesIds do
      out.writeVarInt(t)
}
object TerrainData extends PacketParser[TerrainData](2) {
  def readData(in: NiolInput) =
    val levelId = in.readShort()
    val levelName = in.readVarString()
    val width = in.readShort()
    val height = in.readShort()
    val spawn = in.readVector()
    val exit = in.readVector()
    val tiles = new Array[Int](width*height)
    for i <- 0 until tiles.length do
      tiles(i) = in.readVarInt()
    TerrainData(levelId, levelName, width, height, spawn, exit, tiles)
}
