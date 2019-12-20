package com.electronwill.why.protocol
package client

/** Marker trait for packets sent by the client to the server. */
trait ClientPacket extends Packet
object ClientPacket extends PacketRegistry[ClientPacket] {
  override def init() =
    register(ConnectionRequest)
    register(PlayerMove)
    register(Disconnect)
}
