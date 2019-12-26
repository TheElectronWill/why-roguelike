package com.electronwill
package why.client

import gametype._
import why.gametype.{EntityId, RegisteredType}
import why.protocol.Warning
import why.protocol.server._
import why.{Logger, Grid}

/** Reacts to received packets. */
object PacketHandler {
  def handle(p: ServerPacket): Unit = p match
    case ConnectionResponse(accepted, serverVersion, msg) =>
      Logger.info(s"Server version: $serverVersion, client version: ${Client.VERSION}")
      if !accepted
        Logger.error(s"Connection refused by the server: $msg")
        Client.network.stop()
      else
        Logger.info(s"Connection accepted by the server: $msg")

    case EntityAppearance(entityId, color) =>
      withEntity(entityId, e => {
        Logger.info(s"Set appearance of entity #${entityId}")
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
      val tpe = Entities.get(typeId)
      val entity = ClientEntity(entityId, tpe)
      Client.level.addEntity(entity, position)

      if tpe.name == "player" && position == Client.level.spawnPosition
        Client.player = entity
        Logger.ok("Player instance created!")

        Logger.info("Initializing player's view...")
        Client.initView()
        Logger.ok("Player's view initialized!")

    case IdRegistration(tiles, entities) =>
      Logger.info(s"Registering ${tiles.length} types of tiles and ${entities.length} types of entities.")

      for t <- tiles do
        Tiles.register(t)
        Logger.info(s"-> $t")

      for t <- entities do
        Entities.register(t)
        Logger.info(s"-> $t")

      Logger.ok("Types registered!")

    case TerrainData(levelId, levelName, width, height, spawn, exit, tilesIds) =>
      val terrain = Grid[RegisteredType](width, height, Tiles.get("void"))
      for (id, i) <- tilesIds.zipWithIndex do
        val x = i % width
        val y = i / width
        terrain(x, y) = Tiles.get(id)
      Client.level = ClientDungeonLevel(levelId, levelName, spawn, exit, terrain)
      Logger.ok(s"Dungeon level set to $levelName")

    case TileUpdate(position, tileId) =>
      Client.level.terrain(position) = Tiles.get(tileId)

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
