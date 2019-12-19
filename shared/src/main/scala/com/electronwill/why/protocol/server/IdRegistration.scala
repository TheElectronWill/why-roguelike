package com.electronwill
package why.protocol
package server

import niol._
import why.gametype._

case class IdRegistration(tiles: Array[TileTypeData], entities: Array[EntityTypeData]) extends Packet(1) {
  def writeData(out: NiolOutput): Unit =
    out.writeVarInt(tiles.length)
    for t <- tiles do
      out.writeVarInt(t.id)
      out.writeVarString(t.name)
      out.writeChar(t.defaultChar)
      out.writeBool(t.isBlock)

    out.writeVarInt(entities.length)
    for e <- tiles do
      out.writeVarInt(e.id)
      out.writeVarString(e.name)
      out.writeChar(e.defaultChar)
}
object IdRegistration extends PacketParser[IdRegistration](1) {
  def readData(in: NiolInput) =
    val tiles = new Array[TileTypeData](in.readVarInt())
    for i <- 1 to tiles.length do
      tiles(i) = TileTypeData(
        in.readVarInt(),
        in.readVarString(),
        in.readChar(),
        in.readBool()
      )
    val entities = new Array[EntityTypeData](in.readVarInt())
    for i <- 1 to entities.length do
      entities(i) = EntityTypeData(
        in.readVarInt(),
        in.readVarString(),
        in.readChar()
      )
    IdRegistration(tiles, entities)
}
