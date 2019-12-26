package com.electronwill.why

import Vec2i._
import scala.annotation.alpha
import scala.reflect.ClassTag

final class Grid[A : ClassTag](val width: Int,
                               val height: Int,
                               private val default: A) {
  require(width > 0 && height > 0)

  /** internal storage of A */
  private val storage = Array.fill[A](height * width)(default)

  /** @return the index of a position in the internal array */
  private inline def idx(pos: Vec2i): Int = width*pos.y + pos.x
  private inline def idx(x: Int, y: Int): Int = width*y + x

  def apply(pos: Vec2i): A = storage(idx(pos))
  def apply(x: Int, y: Int): A = storage(idx(x, y))

  def update(pos: Vec2i, t: A): Unit = update(pos.x, pos.y, t)

  def update(x: Int, y: Int, t: A): Unit =
    if t == null && default != null
      throw NullPointerException(s"Illegal null value for this grid.")
    else
      storage(idx(x, y)) = t

  def remove(pos: Vec2i): Unit = update(pos, default)
  def remove(x: Int, y: Int): Unit = update(x, y, default)

  def move(pos: Vec2i, to: Vec2i): Unit = {
    val origin = idx(pos)
    val dest = idx(to)
    storage(dest) = storage(origin)
    storage(origin) = default
  }

  def isValid(pos: Vec2i): Boolean = isValid(pos.x, pos.y)
  def isValid(x: Int, y: Int): Boolean =
    x >= 0 && x < width && y >= 0 && y < height


  /** Gets the content of the tiles around the position, in the following order:
    * (up, right, down, left)
    */
  def around(pos: Vec2i): (A,A,A,A) = (UP, RIGHT, DOWN, LEFT).tmap(v =>
    if isValid(pos+v) then apply(pos+v) else default
  )
  def around(x: Int, y: Int): (A,A,A,A) = around(Vec2i(x, y))

  def squareAround(center: Vec2i, radius: Int): Seq[A] =
    for
      x <- center.x-radius to center.x+radius
      y <- center.y-radius to center.y+radius
      if isValid(x, y)
    yield
      this(Vec2i(x, y))
}
