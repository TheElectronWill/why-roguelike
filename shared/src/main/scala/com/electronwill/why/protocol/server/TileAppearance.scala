package com.electronwill
package why.protocol
package server

import niol._
import why.util.Vec2i

case class TileAppearance(position: Vec2i, newChar: Char) extends Packet(8) {
  // TODO color: ColorSetting
  def writeData(out: NiolOutput): Unit =
    out.writeVector(position)
    out.writeChar(newChar)
}
object TileAppearance extends PacketParser[TileAppearance](8) {
  def readData(in: NiolInput) =
    TileAppearance(in.readVector(), in.readChar())
}
