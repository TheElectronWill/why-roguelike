package com.electronwill
package why.protocol
package client

import niol._
import why.util.Vec2i
import why.{Tile, Entity}

case class IdRegistration(tiles: Seq[Tile], entities: Seq[Entity]) extends Packet(1) {
  def writeData(out: NiolOutput): Unit =
    // TODO use TileType instead of Tile here
    out.writeVarInt(tiles.length)
    for t <- tiles do
      out.writeVarInt(t.id)
      out.writeChar(t.char)
    // TODO use EntityType instead of Entity here
    out.writeVarInt(entities.length)
    for t <- entities do
      out.writeVarInt(t.id)
      out.writeChar(t.char)
}
object IdRegistration extends PacketParser[IdRegistration](1) {
  def readData(in: NiolInput) =
    val tileCount = in.readVarInt()
    val entityCount = in.readVarInt()
    val tileTypes = Seq[Tile]() // TODO
    val entityTypes = Seq[Entity]() // TODO
    IdRegistration(tileTypes, entityTypes)
}
