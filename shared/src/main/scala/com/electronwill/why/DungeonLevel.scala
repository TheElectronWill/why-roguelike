package com.electronwill
package why

import gametype._
import collection.RecyclingIndex
import scala.collection.mutable.LongMap

class DungeonLevel(val level: Int,
                   val name: String,
                   val spawnPosition: Vec2i,
                   val exitPosition: Vec2i,
                   val terrain: Grid[Tile],
                   private val entityGrid: Grid[Entity]) {
  require(terrain.width == entityGrid.width)
  require(terrain.height == entityGrid.height)

  private var entityIds = RecyclingIndex[Entity]()

  def width = terrain.width
  def height = terrain.height

  def getEntity(id: EntityId): Entity = entityIds(id.id)

  def addEntity(e: Entity, at: Vec2i): EntityId =
    entityGrid(at) = e
    EntityId(entityIds += e)

  def deleteEntity(id: EntityId): Unit =
    val entity = getEntity(id)
    entityGrid(entity.position) = null
    entityIds.remove(id.id)

  def moveEntity(id: EntityId, to: Vec2i): Unit =
    val entity = getEntity(id)
    entityGrid.move(entity.position, to)

  def getEntity(at: Vec2i) = Option(entityGrid(at))
}
