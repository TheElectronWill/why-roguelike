package com.electronwill
package why
package server

import gametype._
import network.{NetworkSystem, WhyClientAttach}

import nightconfig.core.file.FileConfig
import scala.collection.mutable.LongMap

object Server {
  inline val VERSION = "1.0.0-beta1"
  inline val VERSION_MAJOR = "1"

  val config = FileConfig.builder("server-config.toml").defaultResource("/default-config.toml").sync().build()
  config.load()

  private val levels = LongMap[ServerDungeonLevel]() // int -> loaded level
  private val generator = gen.BspGenerator(80, 20, 1000, 1000, 2) // generates levels
  private val playersByClient = LongMap[Player]() // client id -> player entity

  val port: Int = config.get("port")
  val message: String = config.get("motd")
  val network = NetworkSystem(port)

  def registerTypes(): Unit =
    Logger.info("Registering types...")
    Logger.info(s"Tiles.Void is ${Tiles.Void}")
    Logger.info("(Skipped other tiles)")
    Logger.info(s"Entities.Player is ${Entities.Player}")
    Logger.ok(s"Types registered: ${Tiles.size} tiles and ${Entities.size} entities")

  def getOrCreateLevel(n: Int): ServerDungeonLevel =
    levels.getOrElseUpdate(n.toLong, generateLevel(n))

  private def generateLevel(n: Int) =
    val lvl = generator.generate(n)
    Logger.ok(s"Level $n generated")
    lvl

  /** All the connected players. */
  def players: Iterable[Player] = playersByClient.values

  def registerPlayer(player: Player, client: WhyClientAttach): Unit =
    playersByClient(client.clientId) = player

  def getPlayer(client: WhyClientAttach): Option[Player] =
    playersByClient.get(client.clientId)

  def removePlayer(client: WhyClientAttach): Unit =
    val player = playersByClient.remove(client.clientId)
    player match
      case None =>
        Logger.warn(s"Player ${client.clientId} has already been removed!")
      case Some(p) =>
        p.level.deleteEntity(p)
        client.disconnect()
        Logger.ok(s"Removed player ${client.clientId}")
}
