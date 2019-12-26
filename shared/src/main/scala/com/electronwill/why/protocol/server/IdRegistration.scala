package com.electronwill
package why.protocol
package server

import niol._
import why.gametype._

case class IdRegistration(tiles: Array[RegisteredType], entities: Array[RegisteredType]) extends ServerPacket(1) {
  def writeData(out: NiolOutput): Unit =
    out.writeVarInt(tiles.length)
    tiles.foreach(out.writeType(_))

    out.writeVarInt(entities.length)
    entities.foreach(out.writeType(_))
}
object IdRegistration extends PacketParser[IdRegistration](1) {
  def readData(in: NiolInput) =
    val tiles = new Array[RegisteredType](in.readVarInt())
    for i <- 0 until tiles.length do
      tiles(i) = in.readType()

    val entities = new Array[RegisteredType](in.readVarInt())
    for i <- 0 until entities.length do
      entities(i) = in.readType()

    IdRegistration(tiles, entities)
}
