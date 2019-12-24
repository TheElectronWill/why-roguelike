package com.electronwill
package why.protocol
package server

import niol._
import why.Vec2i

case class TerrainData(levelId: Int, levelName: String, width: Int, height: Int, tilesIds: Array[Int], spawn: Vec2i, exit: Vec2i) extends ServerPacket(2) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(levelId)
    out.writeVarString(levelName)
    out.writeShort(width)
    out.writeShort(height)
    out.writeInts(tilesIds)
    out.writeVector(spawn)
    out.writeVector(exit)
}
object TerrainData extends PacketParser[TerrainData](2) {
  def readData(in: NiolInput) =
    val levelId = in.readShort()
    val levelName = in.readVarString()
    val width = in.readShort()
    val height = in.readShort()
    val tiles = new Array[Int](width*height)
    for i <- 0 until tiles.length do
      tiles(i) = in.readVarInt()
    val spawn = in.readVector()
    val exit = in.readVector()
    TerrainData(levelId, levelName, width, height, tiles, spawn, exit)
}
