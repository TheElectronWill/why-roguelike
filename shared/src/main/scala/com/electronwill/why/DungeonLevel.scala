package com.electronwill
package why

import gametype._
import collection.RecyclingIndex
import scala.collection.mutable.LongMap
import scala.reflect.ClassTag

abstract class DungeonLevel[T <: Tile, E >: Null <: Entity : ClassTag](
    val number: Int,
    val name: String,
    val spawnPosition: Vec2i,
    val exitPosition: Vec2i,
    val terrain: Grid[T],
  ) {

  protected val entityGrid = Grid[E](width, height, null)
  protected val entityIds = RecyclingIndex[E]()

  def width: Int = terrain.width
  def height: Int = terrain.height

  def getEntity(id: EntityId): Option[E] = entityIds.get(id.id)
  def getEntity(at: Vec2i): Option[E] = Option(entityGrid(at))

  def moveEntity(e: E, to: Vec2i): Unit =
    entityGrid.move(e.position, to)
    e.position = to

  def addEntity(e: E, at: Vec2i): Unit

  def deleteEntity(e: E): Unit
}
