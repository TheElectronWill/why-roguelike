package com.electronwill
package why.protocol
package client

import why.geom._
import niol._

case class PlayerMove(dst: Vec2i) extends Packet(1) with ClientPacket {
  def writeData(out: NiolOutput): Unit = out.writeVector(dst)
}
object PlayerMove extends PacketParser[PlayerMove](1) {
  def readData(in: NiolInput) = PlayerMove(in.readVector())
}
