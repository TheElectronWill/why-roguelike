package com.electronwill.why.util

final class Grid[A : ClassTag](val width: Int,
                               val height: Int,
                               private val default: A) {
  require(width > 0 && height > 0)

  /** internal storage of A */
  private val storage = Array[A](height * width)

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
  def -= (pos: Vec2i): Unit = storage(pos) = default

  def isValid(pos: Vec2i): Boolean =
    pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height

}