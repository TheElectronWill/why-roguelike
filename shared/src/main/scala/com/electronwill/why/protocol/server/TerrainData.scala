package com.electronwill
package why.protocol
package server

import niol._
import why.Vec2i

case class TerrainData(levelId: Int, width: Int, height: Int, tilesIds: Array[Int], spawn: Vec2i, exit: Vec2i) extends ServerPacket(2) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(levelId)
    out.writeShort(width)
    out.writeShort(height)
    out.writeInts(tilesIds)
    out.writeVector(spawn)
    out.writeVector(exit)
}
object TerrainData extends PacketParser[TerrainData](2) {
  def readData(in: NiolInput) =
    val levelId = in.readShort()
    val width = in.readShort()
    val height = in.readShort()
    val tiles = new Array[Int](width*height)
    in.readInts(tiles)
    val spawn = in.readVector()
    val exit = in.readVector()
    TerrainData(levelId, width, height, tiles, spawn, exit)
}
