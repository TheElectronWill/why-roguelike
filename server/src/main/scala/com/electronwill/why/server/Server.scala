package com.electronwill
package why
package server

import niol.network.tcp._ // com.electronwill.niol
import niol.buffer.storage._

import nightconfig.core.file.FileConfig

import gametype._
import why.gametype._
import why.DungeonLevel
import why.protocol._
import why.protocol.server._
import why.protocol.client._

import scala.collection.mutable.LongMap

object Server {
  inline val VERSION = "1.0.0-beta1"
  inline val VERSION_MAJOR = "1"

  val config = FileConfig.builder("server-config.toml").defaultResource("/default-config.toml").sync().build()
  config.load()

  private val levels = LongMap[ServerDungeonLevel]() // int -> loaded level
  private val generator = gen.BspGenerator(50, 40, 1000, 1000, 2) // generates levels
  private val playersByClient = LongMap[Player]() // client id -> player entity

  val port: Int = config.get("port")
  val message: String = config.get("motd")
  val network = NetworkSystem(port)

  def registerTypes(): Unit =
    Logger.info("Registering types...")
    Logger.info(s"Tiles.Void is ${Tiles.Void}")
    Logger.info("(Skipped other tiles)")
    Logger.info(s"Entities.Player is '${Entities.Player.defaultChar}'")
    Logger.ok(s"Types registered: ${TileType.allTypes.size} tiles and ${EntityType.allTypes.size} entities")

  def getOrCreateLevel(n: Int): ServerDungeonLevel =
    levels.getOrElseUpdate(n.toLong, generator.generate(n))

  /** Gets all the players in the same level as this player */
  def levelMates(player: Player) =
    for other <- playersByClient.values
    if other != player && other.level == player.level
    yield other

  def registerPlayer(player: Player, client: WhyClientAttach): Unit =
    playersByClient(client.clientId) = player

  def getPlayer(client: WhyClientAttach): Option[Player] =
    playersByClient.get(client.clientId)

  def removePlayer(client: WhyClientAttach): Unit =
    val player = playersByClient.remove(client.clientId)
    for
      p <- player
      other <- levelMates(p)
    do
      other.client.sendPacket(EntityDelete(p.id))
}
