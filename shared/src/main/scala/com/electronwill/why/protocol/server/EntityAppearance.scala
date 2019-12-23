package com.electronwill
package why.protocol
package server

import niol._
import why.Vec2i
import why.ansi.ColorSetting

case class EntityAppearance(entityId: Int, newChar: Char, color: ColorSetting) extends ServerPacket(6) {
  def writeData(out: NiolOutput): Unit =
    out.writeShort(entityId)
    out.writeChar(newChar)
    out.writeColorSetting(color)
}
object EntityAppearance extends PacketParser[EntityAppearance](6) {
  def readData(in: NiolInput) =
    EntityAppearance(in.readUnsignedShort(), in.readChar(), in.readColorSetting())
}
