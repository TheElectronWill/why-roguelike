package com.electronwill
package why.server

import why.gametype._
import why.{DungeonLevel, Vec2i, Grid, Logger}
import why.protocol.server.{ServerPacket, EntitySpawn, EntityDelete, EntityMove}
import gametype.{Entities, ServerEntity, Player}
import collection.RecyclingIndex
import scala.collection.mutable.LongMap

class ServerDungeonLevel(number: Int, name: String, spawn: Vec2i, exit: Vec2i, terrain: Grid[RegisteredType])
  extends DungeonLevel[ServerEntity](number: Int, name, spawn, exit, terrain) {

  /** The players currently in this level. */
  def players: Iterable[Player] =
    for (id,entity) <- entityIds
    if entity.tpe == Entities.Player
    yield entity.asInstanceOf[Player]

  /** Sends a packet to all the players except one
    * (or to all the players if `except` is not a player or is null).
    */
  def notifyPlayers(packet: ServerPacket, except: ServerEntity) =
    for p <- players if p != except do
      p.client.sendPacket(packet)

  /** Spawns an entity and notifies the players who are in this level. */
  def addEntity(e: ServerEntity, at: Vec2i): Unit =
    entityGrid(at) = e
    val id = EntityId(entityIds += e)
    e.init(this, at, id)
    val spawnPacket = EntitySpawn(id, e.tpe.id, at)
    notifyPlayers(spawnPacket, e)

  /** Deletes an entity and notifies the players who are in this level. */
  def deleteEntity(e: ServerEntity): Unit =
    val id = e.id
    entityGrid(e.position) = null
    entityIds.remove(id.id)
    e.deinit()
    val despawnPacket = EntityDelete(id)
    notifyPlayers(despawnPacket, null)

  /** Moves an entity and notifies the relevant players. */
  override def moveEntity(e: ServerEntity, to: Vec2i): Unit =
    super.moveEntity(e, to)
    val movePacket = EntityMove(e.id, to)
    notifyPlayers(movePacket, e)
}
