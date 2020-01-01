package com.electronwill
package why.protocol
package server

import niol._
import why.geom.Vec2i
import why.ansi.ColorSetting
import why.gametype.EntityId

case class EntityAppearance(entityId: EntityId, color: ColorSetting) extends ServerPacket(6) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId.id)
    out.writeColorSetting(color)
}
object EntityAppearance extends PacketParser[EntityAppearance](6) {
  def readData(in: NiolInput) =
    EntityAppearance(EntityId(in.readUnsignedShort()), in.readColorSetting())
}
