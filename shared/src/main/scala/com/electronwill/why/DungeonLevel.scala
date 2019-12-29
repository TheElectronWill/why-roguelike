package com.electronwill
package why

import gametype._
import collection.RecyclingIndex
import scala.collection.mutable.{ArrayBuffer, LongMap}
import scala.reflect.ClassTag

abstract class DungeonLevel[E >: Null <: Entity : ClassTag](
    val number: Int,
    val name: String,
    val spawnPosition: Vec2i,
    val exitPosition: Vec2i,
    val terrain: Grid[RegisteredType],
  ) {

  protected val entityGrid = Grid[E](width, height, null)
  protected val entityIds = RecyclingIndex[E](128)

  def width: Int = terrain.width
  def height: Int = terrain.height

  def entityCount: Int = entityIds.size
  def entities: Iterable[E] = for (id,e) <- entityIds yield e

  def getEntity(id: EntityId): Option[E] = entityIds.get(id.id)
  def getEntity(at: Vec2i): Option[E] = Option(entityGrid(at))
  def getEntity(x: Int, y: Int): Option[E] = Option(entityGrid(x, y))

  def moveEntity(e: E, to: Vec2i): Unit =
    entityGrid.move(e.position, to)
    e.position = to

  def addEntity(e: E, at: Vec2i): Unit
  def deleteEntity(e: E): Unit

  def isBlocked(x: Int, y: Int): Boolean =
    getEntity(x,y).map(_.tpe.isBlocking).getOrElse(terrain(x,y).isBlocking)

  def isBlocked(pos: Vec2i): Boolean = isBlocked(pos.x, pos.y)

  export terrain.isValid

  /** Computes a segment aligned with the grid. Returns the ending point (inclusive). */
  def castLine(start: Vec2i, dir: Direction, maxLength: Int, blockingPower: Int = Int.MaxValue/2): Vec2i =
    var p = start
    var next = Vec2i.ZERO
    var i = 0
    while
      next = p + dir.vector
      i += (if isBlocked(next) then blockingPower else 1)
      i < maxLength
    do p = next
    p

  /** Exact ray casting (there is no fixed step, it could cause misses).
    * The returned Iterable is guaranteed to have a `knownSize`.
    * @param start starting point
    * @param dir direction
    * @param until stop condition
    * @return all the traversed locations; the last one does fulfill the stop condition
    * @see https://theshoemaker.de/2016/02/ray-casting-in-2d-grids/
    */
  def castRay(start: Vec2f, dir: Vec2f, until: (Int,Int) => Boolean = isBlocked): Iterable[Vec2i] =
    val signDX = dir.x.sign.toInt
    val signDY = dir.y.sign.toInt
    val tileStepX = if signDX == 1 then 1 else 0
    val tileStepY = if signDY == 1 then 1 else 0

    val traversed = ArrayBuffer[Vec2i]()
    var tileX = start.x.toInt
    var tileY = start.y.toInt
    var p = start

    while isValid(tileX, tileY) && !until(tileX, tileY) do
      val dtX = (tileX + tileStepX - p.x) / dir.x // how many `dir` to add to go to the next tile in X
      val dtY = (tileY + tileStepY - p.y) / dir.y // how many `dir` to add to go to the next tile in Y
      if dtX < dtY
        // Move in the X direction
        tileX = tileX + signDX
        p = p + dir*dtX
        traversed += Vec2i(tileX, tileY)
      else
        // Move in the Y direction
        tileY = tileY + signDY
        p = p + dir*dtY
        traversed += Vec2i(tileX, tileY)
    traversed
}
