package com.electronwill
package why.client

import gametype._
import why.gametype.EntityId
import why.protocol.Warning
import why.protocol.server._
import why.Logger

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
      withEntity(entityId, e => {
        Logger.info(s"Set appearance of entity #${entityId}")
        e.character = char
        e.customColor = color
      })

    case EntityDelete(entityId) =>
      withEntity(entityId, e => {
        Logger.info(s"Removed entity #${e.id} of type ${e.tpe}")
        Client.level.deleteEntity(e)
      })

    case EntityMove(entityId, dst) =>
      withEntity(entityId, Client.level.moveEntity(_, dst))

    case EntitySpawn(entityId, typeId, position) =>
      val tpe = EntityType.typeWithId(typeId)
      val entity = ClientEntity(entityId, tpe)

  private def withEntity(entityId: EntityId, f: ClientEntity => Unit) =
    val entity = Client.level.getEntity(entityId).asInstanceOf[ClientEntity]
    if entity == null
      noSuchEntity(entityId)
    else
      f(entity)

  private def noSuchEntity(entityId: EntityId) =
    val msg = s"No entity with id ${entityId}"
    Logger.error(msg)
    Client.network.send(Warning(msg))
}
