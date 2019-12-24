package com.electronwill
package why.server

import gametype._
import why._
import why.gametype._
import why.protocol._
import why.protocol.client._
import why.protocol.server._

object PacketHandler {
  def handlePacket(p: ClientPacket, client: WhyClientAttach) =
    // TODO register listeners (and use separate files for them) instead of putting all the code in one big match/case
    Logger.info(s"Received packet from client ${client.clientId}: $p")
    p match
      case ConnectionRequest(clientVersion, username) =>
        if clientVersion.split(".")(0) != Server.VERSION_MAJOR
          // TODO parse the version correctly (to integers) and use full semantic
          // versioning to compare (e.g. take -beta suffix into account)
          val byebye = ConnectionResponse(false, 0, Server.VERSION, s"Your client version is NOT COMPATIBLE with the server")
          client.sendPacket(byebye, () => client.disconnect())
        else
          // 1: accept the connection
          Logger.info(s"New client connected with id ${client.clientId}")
          val player = Player(client)
          val welcomePacket = ConnectionResponse(true, player.id.id, Server.VERSION, "Welcome to WHY - by TheElectronWill")
          client.sendPacket(welcomePacket)

          // 2: send the types data
          val tilesData = TileType.allTypes.map(t => TileTypeData(t.id, t.make().name, t.defaultChar, t.make().isBlock)).toArray
          val entitiesData = EntityType.allTypes.map(t => EntityTypeData(t.id, "Player", t.defaultChar)).toArray
          val registrationPacket = IdRegistration(tilesData, entitiesData)
          client.sendPacket(registrationPacket)

          // 3: send the terrain data
          val gameLevel = Server.getOrCreateLevel(1) // generates the level if needed
          sendTerrainData(gameLevel, client)

          // 4: register the player
          Server.registerPlayer(player, client)
          val level = Server.getOrCreateLevel(1)
          level.addEntity(player, level.spawnPosition)
          player.position = level.spawnPosition
          player.level = level

          // 5: notify the other players (in the same level)
          val spawnPacket = EntitySpawn(player.id, Entities.Player.id, player.position)
          Server.levelMates(player).foreach(_.client.sendPacket(spawnPacket))

      case PlayerMove(destination: Vec2i) =>
        Server.getPlayer(client) match
          case None =>
            Logger.error("Unexpected packet PlayerMove, expected ConnectionRequest.")
            val byebye = Disconnect(true, "Unexpected packet PlayerMove, please send ConnectionRequest first!")
            client.sendPacket(byebye, ()=>client.disconnect())
          case Some(player) =>
            if player.level.terrain(destination).tpe == Tiles.Stairs
              // handle stairs (go to the next level)
              // remove the player from this level for those who still are there
              val despawnPacket = EntityDelete(player.id)
              Server.levelMates(player).foreach(_.client.sendPacket(despawnPacket))
              val levelId = player.level.number
              player.level = null // the player has no level until the new one is ready

              // generates (if needed) the next level and moves the player to it
              val nextLevel = Server.getOrCreateLevel(levelId+1)
              player.level = nextLevel
              player.position = nextLevel.spawnPosition
              sendTerrainData(nextLevel, client)

              // notify the players of the next level
              val spawnPacket = EntitySpawn(player.id, Entities.Player.id, player.position)
              Server.levelMates(player).foreach(_.client.sendPacket(spawnPacket))
            else
              // handle non-stairs (move in the same level)
              player.level.moveEntity(player, destination)
              player.position = destination

              // notify the other players
              val movePacket = EntityMove(player.id, destination)
              Server.levelMates(player).foreach(_.client.sendPacket(movePacket))

  def sendTerrainData(level: ServerDungeonLevel, client: WhyClientAttach) =
    val (width, height) = (level.width, level.height)
    val spawn = level.spawnPosition
    val exit = level.exitPosition

    val tilesIds = new Array[Int](width * height)
    for
      x <- 0 to width
      y <- 0 to height
    do
      tilesIds(width*x + y) = level.terrain(x, y).tpe.id

    val terrainData = TerrainData(level.number, width, height, tilesIds, spawn, exit)
    client.sendPacket(terrainData)
}
