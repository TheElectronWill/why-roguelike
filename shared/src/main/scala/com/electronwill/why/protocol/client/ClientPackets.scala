package com.electronwill.why.protocol
package client

/**
 * Packets sent by the client to the server.
 */
object ClientPackets extends PacketRegistry {
  override def init() =
    register(ConnectionRequest)
    register(PlayerMove)
    register(Disconnect)
}
