package com.electronwill.why.geom

import scala.util.Random

final class Box private(private val xmin: Int, private val ymin: Int, private val xmax: Int, private val ymax: Int) {
  def xMin: Int = xmin
  def xMax: Int = xmax
  def yMin: Int = ymin
  def yMax: Int = ymax

  def xInterval = (xmin, xmax)
  def yInterval = (ymin, ymax)

  def cornerMin = Vec2i(xmin, ymin)
  def cornerMax = Vec2i(xmax, ymax)

  def roundedCenter = Vec2i((xmin+xmax)/2, (ymin+ymax)/2)
  def realCenter = ((xmin+xmax)/2.0, (ymin+ymax)/2.0)

  def height = ymax-ymin
  def width = xmax-xmin

  def isTall = height > width
  def isWide = height < width
  def isSquare = height == width

  def randomPoint = Vec2i(Random.between(xMin, xMax+1), Random.between(yMin, yMax+1))

  def randomBorderPoint =
    val x = Random.between(xMin, xMax+1)
    val y =
      if x == xMin || x == xMax then Random.between(yMin, yMax+1)
      else if Random.nextBoolean() then yMin
      else yMax
    Vec2i(x, y)

  def shift(x: Int, y: Int) = Box(xmin+x, ymin+y, xmax+x, ymax+y)

  /** Creates two sub-boxes by splitting this box in two.
    * By default the box is cut along its longer axis.
    * @param horitontally to cut horizontally
    * @return two sub-boxes
    */
  def split(horizontally: Boolean = isTall): (Box, Box) =
    val center = roundedCenter
    if horizontally
      (
        Box.intervals(xInterval, (center.y + 1, yMax)),
        Box.intervals(xInterval, (yMin, center.y))
      )
    else
      (
        Box.intervals((center.x + 1, xMax), yInterval),
        Box.intervals((xMin, center.x), yInterval)
      )

  override def toString(): String = s"[$xMin, $yMin] x [$xMax, $yMax]"
}
object Box {
  def corners(min: Vec2i, max: Vec2i) = Box(min.x, min.y, max.x, max.y)
  def intervals(x: (Int, Int), y: (Int, Int)) = Box(x._1, y._1, x._2, y._2)

  def around(c: Vec2i, width: Int, height: Int) =
    val toAdd = Vec2i(math.max(0, width-1), math.max(0, height-1)) // without the center
    val a = toAdd / 2 // rounded down
    val b = toAdd - a // what remains
    Box.corners(c-a, c+b)

  def center(c: Vec2i, widthRadius: Int, heightRadius: Int) =
    val vec = Vec2i(widthRadius, heightRadius)
    Box.corners(c-vec, c+vec)

  def positive(width: Int, height: Int) = Box(0, 0, width, height)
}
