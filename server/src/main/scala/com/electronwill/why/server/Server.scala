package com.electronwill
package why.server

import niol.network.tcp._ // com.electronwill.niol
import niol.buffer.storage._

import gametype._
import why.gametype._
import why.DungeonLevel
import why.util.Vec2i
import why.protocol._
import why.protocol.server._
import why.protocol.client._
import scala.collection.mutable.LongMap

object Server {
  inline val VERSION = "1.0.0-beta1"
  inline val VERSION_MAJOR = "1"

  private val levels = LongMap[DungeonLevel]() // int -> loaded level
  private val generator = gen.BspGenerator(50, 40, 1000, 1000, 2) // generates levels
  private val playersByClient = LongMap[Player]() // client id -> player entity

  private var port = -1
  private var selector: ScalableSelector = _
  private val bufferPool = StagedPools().directStage(500, 10, isMoreAllocationAllowed=false).build()
  // Allocate at most 10 buffers of at most 500 bytes of direct memory (off-heap)
  // If an incoming packet has a size > 500 bytes, throw an error.
  // Outgoing packets are not limited: any buffer can be sent to a client with ClientAttach.write(buff)

  /** Starts the TCP server on the given port. */
  def start(port: Int): Unit =
    // Creates the ScalableSelector
    // It will listen to incoming packets and process outgoing ones.
    val onStart = () => println("Server started")
    val onStop = () => println("Server stopped")
    val onError = (e: Exception) => {
      println(s"[ERROR] $e")
      e.printStackTrace()
      false // false to stop the server on error, true to continue
    }
    selector = ScalableSelector(onStart, onStop, onError)

    // Defines how the memory is managed for incoming packets
    val bufferSettings = BufferSettings(baseBufferSize=500, bufferProvider=bufferPool)

    // Registers a new TcpListener to be notified when a new client connects
    // and when a client disconnects.
    val listener = WhyTcpListener()
    selector.listen(port, bufferSettings, listener)
    this.port = port

    // Starts the server
    selector.start("server-thread")
    Thread.sleep(10000)
    selector.stop()

  /** Disconnects all the clients and stops the TCP server. */
  def stop(): Unit = selector.stop()

  // --- --- ---
  def getOrCreateLevel(n: Int): DungeonLevel =
    val generator: gen.WalkingGenerator = null
    levels.getOrElseUpdate(n.toLong, generator.generate(n))

  def handlePacket(p: ClientPacket, client: WhyClientAttach) =
    // TODO register listeners (and use separate files for them) instead of putting all the code in one big match/case
    println(s"[INFO] Received packet from client ${client.clientId}: $p")
    p match
      case ConnectionRequest(clientVersion, username) =>
        if clientVersion.split(".")(0) != VERSION_MAJOR
          // TODO parse the version correctly (to integers) and use full semantic
          // versioning to compare (e.g. take -beta suffix into account)
          val byebye = ConnectionResponse(false, 0, VERSION, s"Your client version is NOT COMPATIBLE with the server")
          client.sendPacket(byebye, () => client.disconnect())
        else
          // 1: accept the connection
          println(s"[INFO] New client connected with id ${client.clientId}")
          val player = Player(client)
          val welcomePacket = ConnectionResponse(true, player.id, VERSION, "Welcome to WHY - by TheElectronWill")
          client.sendPacket(welcomePacket)

          // 2: send the types data
          val tilesData = TileType.allTypes.map(t => TileTypeData(t.id, t.make().name, t.defaultChar, t.make().isBlock)).toArray
          val entitiesData = EntityType.allTypes.map(t => EntityTypeData(t.id, "Player", t.defaultChar)).toArray
          val registrationPacket = IdRegistration(tilesData, entitiesData)
          client.sendPacket(registrationPacket)

          // 3: send the terrain data
          val gameLevel = getOrCreateLevel(1) // generates the level if needed
          sendTerrainData(gameLevel, client)

          // 4: register the player
          playersByClient(client.clientId) = player
          gameLevel.entities(gameLevel.spawnPosition) = player
          player.position = gameLevel.spawnPosition
          player.level = gameLevel

          // 5: notify the other players (in the same level)
          val spawnPacket = EntitySpawn(player.id, Entities.Player.id, player.position)
          levelMates(player).foreach(_.client.sendPacket(spawnPacket))

      case PlayerMove(destination: Vec2i) =>
        playersByClient.get(client.clientId) match
          case None =>
            println("[ERROR] Unexpected game packet, expected ConnectionRequest.")
            val byebye = Disconnect(true, "Unexpected packet PlayerMove, please send ConnectionRequest first!")
            client.sendPacket(byebye, ()=>client.disconnect())
          case Some(player) =>
            if player.level.terrain(destination).tpe == Tiles.Stairs
              // handle stairs (go to the next level)
              // remove the player from this level for those who still are there
              val despawnPacket = EntityDelete(player.id)
              levelMates(player).foreach(_.client.sendPacket(despawnPacket))
              val levelId = player.level.level
              player.level = null // the player has no level until the new one is ready

              // generates (if needed) the next level and moves the player to it
              val nextLevel = getOrCreateLevel(levelId+1)
              player.level = nextLevel
              player.position = nextLevel.spawnPosition
              sendTerrainData(nextLevel, client)

              // notify the players of the next level
              val spawnPacket = EntitySpawn(player.id, Entities.Player.id, player.position)
              levelMates(player).foreach(_.client.sendPacket(spawnPacket))
            else
              // handle non-stairs (move in the same level)
              player.level.entities.move(player.position, destination)
              player.position = destination

              // notify the other players
              val movePacket = EntityMove(player.id, destination)
              levelMates(player).foreach(_.client.sendPacket(movePacket))

  def sendTerrainData(gameLevel: DungeonLevel, client: WhyClientAttach) =
    val (width, height) = (gameLevel.width, gameLevel.height)
    val spawn = gameLevel.spawnPosition

    val tilesIds = new Array[Int](width * height)
    for
      x <- 0 to width
      y <- 0 to height
    do
      tilesIds(width*x + y) = gameLevel.terrain(x, y).tpe.id

    val terrainData = TerrainData(1, width, height, tilesIds, spawn)
    client.sendPacket(terrainData)

  /** Gets all the players in the same level as this player */
  def levelMates(player: Player) =
    for other <- playersByClient.values
    if other != player && other.level == player.level
    yield other
}
