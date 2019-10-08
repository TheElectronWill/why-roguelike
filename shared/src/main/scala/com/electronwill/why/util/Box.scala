package com.electronwill.why

final class Box private(private val xmin: Int, ymin: Int, xmax: Int, ymax: Int) {
  def xMin: Int = xmin
  def xMax: Int = xmax
  def yMin: Int = ymin
  def yMax: Int = ymax

  def cornerMin = Vec2i(xmin, ymin)
  def cornerMax = Vec2i(xmax, ymax)

  def center = Vec2i((xmin+xmax)/2, (ymin+ymax)/2)
  def realCenter = ((xmin+xmax)/2.0, (ymin+ymax)/2.0)

  def height = ymax-ymin
  def width = xmax-xmin

  def isTall = height>width
  def isWide = height<width
  def isSquare = height==width

  override def toString(): String = s"[$xMin, $yMin] x [$xMax, $yMax]"
}
object Box {
  def fromCorners(min: Vec2i, max: Vec2i) = Box(min.x, min.y, max.x, max.y)
  def fromIntervals(x: (Int, Int), y: (Int, Int)) = Box(x._1, y._1, x._2, y._2)
  def fromCenter(c: Vec2i, width: Int, height: Int) = {
    val diff = Vec2i(width/2, height/2)
    Box(c-diff, c+diff)
  }
}
