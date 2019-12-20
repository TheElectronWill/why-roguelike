package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class EntityAppearance(entityId: Int, newChar: Char) extends ServerPacket(6) {
  // TODO color: ColorSetting
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId)
    out.writeChar(newChar)
}
object EntityAppearance extends PacketParser[EntityAppearance](6) {
  def readData(in: NiolInput) =
    EntityAppearance(in.readUnsignedShort(), in.readChar())
}
