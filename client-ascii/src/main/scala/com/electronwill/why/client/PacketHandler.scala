package com.electronwill
package why
package client

import protocol.server._

object PacketHandler {
  def handle(p: ServerPacket): Unit = p match
    case AreaUpdate(from, to, tilesIds) =>
      // TODO
    case ConnectionResponse(accepted, playerId, serverVersion, msg) =>
      // TODO
    case EntityAppearance(entityId, char, color) =>
      // TODO
    case EntityDelete(entityId) =>
      // TODO
    case
}
