package com.electronwill
package why.protocol
package client

import niol._
import why.util.Vec2i

case class ConnectionResponse(accept: Boolean, serverVersion: String, message: String) extends Packet(0) {
  def writeData(out: NiolOutput): Unit =
    out.writeBool(accept)
    out.writeVarString(serverVersion)
    out.writeVarString(message)
}
object ConnectionResponse extends PacketParser[ConnectionResponse](0) {
  def readData(in: NiolInput) =
    ConnectionResponse(in.readBool(), in.readVarString(), in.readVarString())
}
