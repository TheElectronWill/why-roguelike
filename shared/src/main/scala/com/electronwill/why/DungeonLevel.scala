package com.electronwill
package why

import geom.{_, given}
import gametype._
import collection.RecyclingIndex
import scala.collection.mutable.{ArrayBuffer, LongMap}
import scala.collection.{AbstractIterator, Seq}
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

  def tileCenter(pos: Vec2i): Vec2f = pos.toFloating + Vec2f.HALF

  // This defines isValid = terrain.isValid; see http://dotty.epfl.ch/docs/reference/other-new-features/export.html
  export terrain.isValid

  /** Casts a line segment aligned with the XY grid.
    * Gives all the points of the segment + the stopping point.
    * For instance if an obstacle interrupts the line, returns the points before the obstacle + the obstacle.
    * If the line reaches the border of the grid, the last result is outside of the grid (i.e. `grid.isValid(p)` is `false`).
    */
  def castLine(start: Vec2i, dir: Direction, maxLength: Int, blockingPower: Int = Int.MaxValue/2): Iterator[Vec2i] =
    // blockingPower is not set to Int.MaxValue because this would cause overflows
    new AbstractIterator {
      private var p = start
      private var len = 0

      def hasNext = len < maxLength && isValid(p)

      def next =
        val current = p
        len += (if isBlocked(current) then blockingPower else 1)
        p += dir.vector
        current
    }

  /** Casts a line segment aligned with the XY grid. Stops when a blocking tile or entity is encountered. */
  def castLine(start: Vec2i, dir: Direction): Iterator[Vec2i] = castLine(start, dir, Int.MaxValue/2, Int.MaxValue/2)


  /** Exact ray casting (there is no fixed step, it could cause misses).
    * The starting point and the stopping point are included in the result.
    * @param start starting point
    * @param dir direction
    * @param until stop condition
    * @return all the traversed locations; the last one does fulfill the stop condition or is outside of the grid
    * @see https://theshoemaker.de/2016/02/ray-casting-in-2d-grids/
    */
  def castRay(start: Vec2f, dir: Vec2f, until: Vec2i => Boolean = isBlocked): Iterator[Vec2i] =
    new AbstractIterator {
      private val signDX = dir.x.sign.toInt
      private val signDY = dir.y.sign.toInt
      private val tStepX = if signDX == 1 then 1 else 0
      private val tStepY = if signDY == 1 then 1 else 0

      private var first = true
      private var tile = start.rounded
      private var (pX, pY) = (start.x, start.y)

      def hasNext = first || !until(tile) && isValid(tile)
      // NB: In hasNext, `tile` is the previously returned tile, not the next one.

      def next =
        if first
          first = false
          tile // return the first tile
        else
          // How many `dir` to add to reach the next tile towards dir.x and the next tile towards dir.y:
          val dtX = (tile.x + tStepX - pX) / dir.x
          val dtY = (tile.y + tStepY - pY) / dir.y

          // See which tile is the closest and move to it.
          // This guarantees that no tile is skipped.
          if dtX < dtY // The closest next tile is the one with a X change.
            tile = tile.addX(signDX)
            pX += dtX*dir.x
            pY += dtX*dir.y
          else // The closest next tile is the one with a Y change.
            tile = tile.addY(signDY)
            pX += dtY*dir.x
            pY += dtY*dir.y

          tile // return the next tile
    }

  def canReachCenter(p: Vec2i, target: Vec2i): Boolean =
    val pCenter = tileCenter(p)
    val tCenter = tileCenter(target)
    val ray = castRay(pCenter, tCenter-pCenter, t => isBlocked(t) || t == target).toBuffer
    ray.last == target

  def computeVision(viewer: Vec2i, dir: Direction): Seq[Vec2i] =
    // Casts the central line, in front of the starting point
    Logger.info(s"Computing field of view from $viewer towards $dir")
    val start = viewer + dir.vector // in front of `viewer`
    val vision = castLine(start, dir).to(ArrayBuffer)
    val centralObstacle = vision.last
    //Logger.info(s"Central obstacle: $centralObstacle")

    halfVision(vision, viewer, dir, dir.vector + dir.right.vector, centralObstacle)
    halfVision(vision, viewer, dir, dir.vector + dir.left.vector, centralObstacle)
    vision

  /** Computes half of the triangular field of view. Avoids to use full raycasting as much as possible. */
  private def halfVision(buff: ArrayBuffer[Vec2i], origin: Vec2i, dir: Direction, shift: Vec2i, centralObstacle: Vec2i): Unit =
    //Logger.info(s"Computing half vision field with shift = $shift")
    var lineStart = origin + shift
    var useDirect = true
    var closestBlocked = centralObstacle

    while isValid(lineStart) && terrain(lineStart).name != "void" do
      closestBlocked = visionLine(buff, origin, lineStart, dir, closestBlocked, useDirect)
      lineStart += shift
      if closestBlocked.isNotAfter(lineStart, dir)
        //Logger.warn(s"Line start was $lineStart but has been modified because of an obstacle")
        lineStart += dir.vector
        useDirect = false
      else
        useDirect = true
      //Logger.info(s"New line start: $lineStart")
    //Logger.warn("Exit halfVision()")

  /** Computes a line of vision. See `test raycasting.ods`. */
  private def visionLine(visible: ArrayBuffer[Vec2i], visionOrigin: Vec2i, start: Vec2i, dir: Direction, closestBlocked: Vec2i, useDirect: Boolean): Vec2i =
    //Logger.info(s"Computing vision line from $start towards $dir")
    var newClosestBlocked = closestBlocked
    var p = start
    var direct = useDirect
    var shadow = false

    while p.isNotAfter(closestBlocked, dir) && isValid(p) do
      // In direct mode there is no raycasting, the free tiles are visible (trivial case).
      if direct
        visible += p
        if isBlocked(p)
          //Logger.info(s"Switching to non-direct mode after $p")
          direct = false
          shadow = true
          newClosestBlocked = p
      // In non-direct mode, raycasting is needed.
      // However, because p is not after `closestBlocked`, switching back to direct mode is possible.
      else
        if terrain(p).name == "wall"
          visible += p
        if isBlocked(p)
          shadow = true
        else if shadow
          shadow = false
        else
          val reachable = canReachCenter(p, visionOrigin)
          //Logger.info(s"Is $visionOrigin reachable from $p: $reachable")
          if reachable
            //Logger.info(s"Switching to direct mode after $p")
            visible += p
            direct = true // go back to direct mode to avoid raycasting and improve performance
      end if // direct
      p += dir.vector

    // Once p is beyond a previously found obstacle (`closestBlocked`), direct mode doesn't work
    // anymore and a true raycasting is required to get a correct result.
    while isValid(p) && terrain(p).name != "void" do
      // stop if void to avoid raycasting in the non-generated space
      if canReachCenter(p, visionOrigin)
        visible += p
      p += dir.vector

    newClosestBlocked
}
