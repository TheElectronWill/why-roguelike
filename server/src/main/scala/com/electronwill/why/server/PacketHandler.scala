package com.electronwill
package why.server

import gametype._
import why._
import why.gametype._
import why.protocol._
import why.protocol.client._
import why.protocol.server._

object PacketHandler {

  def handlePacket(p: ClientPacket, client: WhyClientAttach): Unit = p match
    case ConnectionRequest(clientVersion, username) =>
      // TODO parse the client version correctly (to integers + a string suffix) and use full semantic
      // versioning to compare the versions (e.g. take -beta suffix into account)
      if clientVersion.split("\\.")(0) != Server.VERSION_MAJOR
        val byebye = ConnectionResponse(false, Server.VERSION, s"Your client version is NOT COMPATIBLE with the server")
        client.sendPacket(byebye, () => client.disconnect())
      else
        acceptConnection(client)

    case PlayerMove(destination: Vec2i) =>
      Server.getPlayer(client) match
        case None =>
          Logger.error("Unexpected packet PlayerMove, expected ConnectionRequest.")
          val byebye = Disconnect(true, "Unexpected packet PlayerMove, please send ConnectionRequest first!")
          client.sendPacket(byebye, ()=>client.disconnect())

        case Some(player) =>
          handleMove(client, player, destination)

    case Disconnect(error, msg) =>
      Server.removePlayer(client)
      if error
        Logger.warn(s"Client disconnected with an error: $msg")

    case Warning(msg) =>
      Logger.warn(s"Client issued a warning: $msg")


  private def handleMove(client: WhyClientAttach, player: Player, destination: Vec2i) =
    val terrain = player.level.terrain

    if !terrain.isValid(destination)
      Logger.warn(s"Invalid move to $destination: out of bounds coordinates")
      client.sendPacket(Warning(s"$destination is an invalid position!"))

    else if terrain(destination).isBlocking
      Logger.warn(s"Invalid move to $destination: this tile is blocking - cheating?")
      client.sendPacket(Warning(s"$destination is a blocking tile!"))

    else if player.position.snakeDist(destination) > 1
      Logger.warn(s"Invalid move from ${player.position} to $destination: travel distance too big - cheating?")
      client.sendPacket(Warning(s"$destination is too far away!"))

    // stairs: go to the next level
    else if terrain(destination) == Tiles.Stairs
      val currentLevel = player.level
      val nextLevel = Server.getOrCreateLevel(currentLevel.number + 1)
      currentLevel.deleteEntity(player)
      sendLevelData(nextLevel, client)
      nextLevel.addEntity(player, nextLevel.spawnPosition)

    // non-stairs: move in the same level
    else
      player.level.moveEntity(player, destination)


  private def acceptConnection(client: WhyClientAttach) =
    // 1: accept the connection
    Logger.info(s"New client connected with id ${client.clientId}")
    val welcomePacket = ConnectionResponse(true, Server.VERSION, "Welcome to WHY - by TheElectronWill")
    client.sendPacket(welcomePacket)

    // 2: send the types data
    Logger.info("Sending types data")
    val tilesData = Tiles.toArray
    val entitiesData = Entities.toArray
    val registrationPacket = IdRegistration(tilesData, entitiesData)
    client.sendPacket(registrationPacket)

    // 3: send the terrain data
    Logger.info("Sending terrain data")
    val gameLevel = Server.getOrCreateLevel(1) // generates the level if needed
    sendLevelData(gameLevel, client)

    // 4: register the player
    Logger.info("Registering the player")
    val player = Player(client)
    Server.registerPlayer(player, client)

    // 5: spawn the player and notify everyone else
    Logger.info("Spawning the player")
    val level = Server.getOrCreateLevel(1)
    level.addEntity(player, level.spawnPosition) // sends EntitySpawn to the other clients
    client.sendPacket(SetPlayerId(player.id)) // tells the client to display the level

    Logger.ok("Player spawned!")


  private def sendLevelData(level: ServerDungeonLevel, client: WhyClientAttach) =
    val (width, height) = (level.width, level.height)
    val spawn = level.spawnPosition
    val exit = level.exitPosition

    val tilesIds = new Array[Int](width * height)
    Logger.info(s"Terrain dimensions: $width x $height = ${tilesIds.length} tiles ")
    for i <- 0 until tilesIds.length do
      val x = i % width
      val y = i / width
      val tileType = level.terrain(x, y)
      tilesIds(i) = tileType.id

    val terrainData = TerrainData(level.number, level.name, width, height, spawn, exit, tilesIds)
    client.sendPacket(terrainData, ()=>Logger.ok("Terrain data sent!"))
}
