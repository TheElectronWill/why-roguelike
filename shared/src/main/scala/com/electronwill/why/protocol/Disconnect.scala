package com.electronwill
package why.protocol

import niol._
import server.ServerPacket
import client.ClientPacket

// Packet 255: Disconnect is common to client and server
case class Disconnect(error: Boolean, msg: String) extends ServerPacket(255) with ClientPacket {
  def writeData(out: NiolOutput): Unit =
    out.writeBool(error)
    out.writeVarString(msg)
}
object Disconnect extends PacketParser[Disconnect](255) {
  def readData(in: NiolInput) =
    Disconnect(in.readBool(), in.readVarString())
}
