package com.elecronwill.why

/** Vector of 2 integer coordinates (immutable). */
final case class Vec2i(x: Int, y: Int) {
  def +(v: Vec2i) = Vec2i(x+v.x, y+v.y)
  def -(v: Vec2i) = Vec2i(x-v.x y-v.y)
  def *(k: Int) = Vec2i(k*x, k*y)
  def unary_-() = Vec2i(-x, -y)

  def squaredNorm: Int = x*x + y*y

  def norm: Double = math.sqrt(squaredNorm)

  def squaredDist(other: Vec2i): Int = (other-this).norm

  def dist(other: Vec2i): Double = math.sqrt(squaredDist(other))

  /** @return the snake/manhattan norm of this vector */
  def snakeNorm: Int = abs(x) + abs(y)

  /** @return the snake/manhattan distance of this vector */
  def snakeDist(other: Vec2i): Int = abs(x-other.x) + abs(y-other.y)

  override def toString(): String = s"($x, $y)"
}

object Vec2i {
  val Right = Vec2i(1,0)
  val Left = Vec2i(-1,0)
  val Up = Vec2i(0,-1)
  val Down = Vec2i(0,1)
}
