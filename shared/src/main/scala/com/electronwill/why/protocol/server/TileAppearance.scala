package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i
import why.ansi.ColorSetting

case class TileAppearance(position: Vec2i, newChar: Char, color: ColorSetting) extends ServerPacket(8) {
  def writeData(out: NiolOutput): Unit =
    out.writeVector(position)
    out.writeChar(newChar)
    out.writeColorSetting(color)
}
object TileAppearance extends PacketParser[TileAppearance](8) {
  def readData(in: NiolInput) =
    TileAppearance(in.readVector(), in.readChar(), in.readColorSetting())
}
