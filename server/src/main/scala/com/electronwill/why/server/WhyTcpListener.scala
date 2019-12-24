package com.electronwill
package why.server

import why.Logger
import niol.network.tcp.{ServerChannelInfos => SCI, _}
import java.nio.channels.{SelectionKey, SocketChannel}

class WhyTcpListener extends TcpListener[WhyClientAttach] {
  /**
   * When a client tries to connect to the server, this method is called.
   * It accepts the connection and attach a new object (of type WhyClientAttach) to the client,
   * so that we can keep track of who is who.
   */
  override def onAccept(sci: SCI[WhyClientAttach], c: SocketChannel, k: SelectionKey): WhyClientAttach =
    Logger.info(s"Accepted client ${c.getRemoteAddress}")
    val attach = new WhyClientAttach(sci, c, k)
    Logger.info(s"Assigned client to id ${attach.clientId}")
    attach

  /** Called when a client disconnects. Nothing to do here. */
  override def onDisconnect(clientAttach: WhyClientAttach) =
    Logger.info(s"Client ${clientAttach.clientId} disconnected")
    Server.removePlayer(clientAttach)

}
