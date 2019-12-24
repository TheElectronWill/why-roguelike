package com.electronwill
package why.server

import why.gametype._
import why.{DungeonLevel, Vec2i, Grid}
import gametype.ServerEntity
import collection.RecyclingIndex
import scala.collection.mutable.LongMap

class ServerDungeonLevel(number: Int, name: String, spawn: Vec2i, exit: Vec2i, terrain: Grid[Tile])
  extends DungeonLevel[Tile, ServerEntity](number: Int, name, spawn, exit, terrain) {

  def addEntity(e: ServerEntity, at: Vec2i): Unit =
    entityGrid(at) = e
    val id = EntityId(entityIds += e)
    e.init(this, at, id)

  def deleteEntity(e: ServerEntity): Unit =
    entityGrid(e.position) = null
    entityIds.remove(e.id.id)
    e.deinit()
}
