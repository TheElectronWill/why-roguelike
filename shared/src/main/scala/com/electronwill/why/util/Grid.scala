package com.electronwill.why.util

import Vec2i._
import scala.annotation.alpha
import scala.reflect.ClassTag

final class Grid[A : ClassTag](val width: Int,
                               val height: Int,
                               private val default: A) {
  require(width > 0 && height > 0)

  /** internal storage of A */
  private val storage = Array.ofDim[A](height * width)

  /** @return the index of a position in the internal array */
  private inline def idx(pos: Vec2i): Int = width*pos.y + pos.x

  def apply(pos: Vec2i): A = storage(idx(pos))

  def update(pos: Vec2i, t: A): Unit = storage(idx(pos)) = t

  def move(pos: Vec2i, to: Vec2i): Unit = {
    val origin = idx(pos)
    val dest = idx(to)
    storage(dest) = storage(origin)
    storage(origin) = default
  }

  @alpha("remove")
  def -= (pos: Vec2i): Unit = storage(idx(pos)) = default

  def isValid(pos: Vec2i): Boolean =
    pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height

  /** Gets the content of the tiles around the position, in the following order:
    * (up, right, down, left)
    */
  def around(pos: Vec2i): (A,A,A,A) = (Up, Right, Down, Left).tmap(v=>this(pos+v))

  def squareAround(center: Vec2i, radius: Int): Iterable[A] =
    for
      x <- center.x-radius to center.x+radius
      y <- center.y-radius to center.y+radius
    yield
      this(Vec2i(x, y))
}
