package com.electronwill
package why.protocol
package client

import niol._
import why.util.Vec2i

case class PlayerMove(dst: Vec2i) extends Packet(1) {
  def writeData(out: NiolOutput): Unit = out.writeVector(dst)
}
object PlayerMove extends PacketParser[PlayerMove](1) {
  def readData(in: NiolInput) = PlayerMove(in.readVector())
}
