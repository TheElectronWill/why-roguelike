package com.electronwill
package why.client

import gametype._
import why.gametype.EntityId
import why.protocol.Warning
import why.protocol.server._
import why.{Logger, Grid}

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
      Client.level.addEntity(entity, position)

    case IdRegistration(tiles, entities) =>
      Logger.info(s"Registering ${tiles.length} types of tiles and ${entities.length} types of entities.")

      for t <- tiles do
        TileType.register(t.id, t)
        Logger.info(s"-> $t")

      for t <- entities do
        EntityType.register(t.id, t)
        Logger.info(s"-> $t")

      Logger.ok("Types registered!")

    case TerrainData(levelId, levelName, width, height, tilesIds, spawn, exit) =>
      val terrain = Grid[ClientTile](width, height, ClientTile(TileType.typeWithId(0)))
      for (id, i) <- tilesIds.zipWithIndex do
        val x = i % width
        val y = i / width
        terrain(x, y) = ClientTile(TileType.typeWithId(id))
      Client.level = ClientDungeonLevel(levelId, levelName, spawn, exit, terrain)
      Logger.ok(s"Dungeon level set to $levelName")

    case TileAppearance(position, newChar, newColor) =>
      val tile = Client.level.terrain(position)
      tile.character = newChar
      tile.customColor = newColor

    case TileUpdate(position, newTypeId) =>
      val tile = Client.level.terrain(position)
      tile.tpe = TileType.typeWithId(newTypeId)

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
