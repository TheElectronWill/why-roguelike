package com.electronwill
package why
package client

import protocol.server._

object PacketHandler {
  def handle(p: ServerPacket): Unit = p match
    case ConnectionResponse(accepted, playerId, serverVersion, msg) =>
      Logger.info(s"Server version: $serverVersion, client version: ${Client.VERSION}")
      if !accepted
        Logger.error(s"Connection refused by the server: $msg")
        Client.network.stop()
      else
        Logger.info(s"Connection accepted by the server: $msg")
        Client.playerId = playerId
    case EntityAppearance(entityId, char, color) =>
      // TODO
    case EntityDelete(entityId) =>
      // TODO
}
