package com.electronwill
package why.protocol
package server

import niol._
import why.geom.Vec2i

case class ConnectionResponse(accept: Boolean, serverVersion: String, message: String) extends ServerPacket(0) {
  def writeData(out: NiolOutput): Unit =
    out.writeBool(accept)
    out.writeVarString(serverVersion)
    out.writeVarString(message)
}
object ConnectionResponse extends PacketParser[ConnectionResponse](0) {
  def readData(in: NiolInput) =
    ConnectionResponse(in.readBool(), in.readVarString(), in.readVarString())
}
