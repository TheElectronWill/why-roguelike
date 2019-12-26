package com.electronwill
package why.server

import why.gametype._
import why.{DungeonLevel, Vec2i, Grid}
import why.protocol.server.{EntitySpawn, EntityDelete}
import gametype.{Entities, ServerEntity, Player}
import collection.RecyclingIndex
import scala.collection.mutable.LongMap

class ServerDungeonLevel(number: Int, name: String, spawn: Vec2i, exit: Vec2i, terrain: Grid[RegisteredType])
  extends DungeonLevel[ServerEntity](number: Int, name, spawn, exit, terrain) {

  def players: Iterator[Player] =
    for entity <- entityIds.valuesIterator
    if entity.tpe == Entities.Player
    yield entity.asInstanceOf[Player]

  /** Spawns an entity and notifies the players who are in this level. */
  def addEntity(e: ServerEntity, at: Vec2i): Unit =
    entityGrid(at) = e
    val id = EntityId(entityIds += e)
    e.init(this, at, id)

    val spawnPacket = EntitySpawn(id, e.tpe.id, at)
    players.foreach(_.client.sendPacket(spawnPacket))
    // Note that if e is a player, it will be notified too

  /** Deletes an entity and notifies the players who are in this level. */
  def deleteEntity(e: ServerEntity): Unit =
    val id = e.id
    entityGrid(e.position) = null
    entityIds.remove(id.id)
    e.deinit()

    val despawnPacket = EntityDelete(id)
    players.foreach(_.client.sendPacket(despawnPacket))
}
