package com.electronwill
package why.protocol

import niol.{NiolInput, NiolOutput}
import why.ansi._
import why.gametype.RegisteredType
import why.geom.Vec2i

/* Extend Niol capabilities to support WHY structures
 * Use import why.protocol._ to get that
 */

given (in: NiolInput) extended with {

  def readVector() = Vec2i(in.readUnsignedShort, in.readUnsignedShort)

  def readColor(): Color =
    val firstByte = in.readByte()
    (firstByte & 0xf0) match
      case 0 =>
        val isBright = (firstByte & 8) == 1 // 8 = 1000
        val colorId  = firstByte & 7 // 7 = 0111
        val color = StandardColor.values(colorId)
        val brightness = if isBright then StandardBrightness.Bright else StandardBrightness.Dark
        Color.Standard(color, brightness)
      case 0xf0 =>
        val colorId = in.readUnsignedByte()
        Color.Extended(colorId)
      case 0xff =>
        val red = in.readUnsignedByte()
        val green = in.readUnsignedByte()
        val blue = in.readUnsignedByte()
        Color.True(red, green, blue)

  def readColorSetting(): ColorSetting =
    val colorFlag = in.readByte()
    val fg = (colorFlag & 2) == 1
    val bg = (colorFlag & 1) == 1
    ColorSetting(
      if fg then Some(in.readColor()) else None,
      if bg then Some(in.readColor()) else None
    )

  def readType(): RegisteredType =
    RegisteredType(in.readVarString(), in.readVarInt(), in.readChar(), in.readBool())
}

given (out: NiolOutput) extended with {

  def writeVector(v: Vec2i): Unit =
    out.writeShort(v.x)
    out.writeShort(v.y)

  def writeColor(c: Color) = c match
    case Color.Standard(id, modifier) =>
      val isBright = if modifier == StandardBrightness.Bright then 1 else 0
      val byte = isBright << 3 | id.ordinal
      out.writeByte(byte)
    case Color.Extended(id) =>
      out.writeByte(0xf0)
      out.writeByte(id)
    case Color.True(r,g,b) =>
      out.writeByte(0xff)
      out.writeByte(r)
      out.writeByte(g)
      out.writeByte(b)

  def writeColorSetting(s: ColorSetting) =
    var colorFlag = 0
    if s.fg.nonEmpty
      colorFlag |= 2
    if s.bg.nonEmpty
      colorFlag |= 1
    out.writeByte(colorFlag)
    s.fg.foreach(out.writeColor(_))
    s.bg.foreach(out.writeColor(_))

  def writeType(tpe: RegisteredType) =
    out.writeVarString(tpe.name)
    out.writeVarInt(tpe.id)
    out.writeChar(tpe.character)
    out.writeBool(tpe.isBlocking)
}
