package com.electronwill
package why.protocol
package client

import niol._
import why.util.Vec2i
import why.gametype._

case class IdRegistration(tiles: Seq[TileType[?]], entities: Seq[EntityType[?]]) extends Packet(1) {
  def writeData(out: NiolOutput): Unit =
    // TODO use TileType instead of Tile here
    out.writeVarInt(tiles.length)
    for t <- tiles do
      out.writeVarInt(t.id)
      // FIXME out.writeChar(t.character)
    // TODO use EntityType instead of Entity here
    out.writeVarInt(entities.length)
    for t <- entities do
      out.writeVarInt(t.id)
      // FIXME out.writeChar(t.character)
}
object IdRegistration extends PacketParser[IdRegistration](1) {
  def readData(in: NiolInput) =
    val tileCount = in.readVarInt()
    val entityCount = in.readVarInt()
    val tileTypes = Seq[TileType[?]]() // TODO
    val entityTypes = Seq[EntityType[?]]() // TODO
    IdRegistration(tileTypes, entityTypes)
}
