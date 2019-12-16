package com.electronwill
package why.protocol
package client

import niol._
import scala.quoted._

case class ConnectionRequest(clientVersion: String, username: String)
    extends Packet(0) {

  def writeData(out: NiolOutput): Unit =
    out.writeVarString(clientVersion)
    out.writeVarString(username)
    
}
object ConnectionRequest extends PacketParser[ConnectionRequest](0) {
  def readData(in: NiolInput) =
    ConnectionRequest(in.readVarString(), in.readVarString())
}
