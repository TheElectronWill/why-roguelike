package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class TerrainData(levelId: Short, width: Short, height: Short, tilesIds: Array[Int], spawn: Vec2i) extends Packet(2) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(levelId)
    out.writeShort(width)
    out.writeShort(height)
    out.writeInts(tilesIds)
    out.writeVector(spawn)
}
object TerrainData extends PacketParser[TerrainData](2) {
  def readData(in: NiolInput) =
    val levelId = in.readShort()
    val width = in.readShort()
    val height = in.readShort()
    val tiles = new Array[Int](width*height)
    in.readInts(tiles)
    val spawn = in.readVector()
    TerrainData(levelId, width, height, tiles, spawn)
}
