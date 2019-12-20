package com.electronwill
package why.server

import niol.network.tcp.{ServerChannelInfos => SCI, _}
import java.nio.channels.{SelectionKey, SocketChannel}

class WhyTcpListener extends TcpListener[WhyClientAttach] {
  /**
   * When a client tries to connect to the server, this method is called.
   * It accepts the connection and attach a new object (of type WhyClientAttach) to the client,
   * so that we can keep track of who is who.
   */
  override def onAccept(sci: SCI[WhyClientAttach], c: SocketChannel, k: SelectionKey): WhyClientAttach =
    println(s"[INFO] Accepted client ${c.getLocalAddress}")
    val attach = new WhyClientAttach(sci, c, k)
    println(s"[INFO] Assigned client to id ${attach.clientId}")
    attach

  /** Called when a client disconnects. Nothing to do here. */
  override def onDisconnect(clientAttach: WhyClientAttach) =
    println(s"[INFO] Client ${clientAttach.clientId} disconnected")

}
