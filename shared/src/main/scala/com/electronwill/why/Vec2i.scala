package com.electronwill.why

import scala.math._
import scala.util.Random

/** Vector of 2 integer coordinates (immutable). */
final case class Vec2i(x: Int, y: Int) {
  def +(v: Vec2i) = Vec2i(x+v.x, y+v.y)
  def -(v: Vec2i) = Vec2i(x-v.x, y-v.y)
  def *(k: Int) = Vec2i(k*x, k*y)
  def unary_-() = Vec2i(-x, -y)

  def squaredNorm: Int = x*x + y*y

  def norm: Double = sqrt(squaredNorm.toDouble)

  def squaredDist(other: Vec2i): Int = (other-this).squaredNorm

  def dist(other: Vec2i): Double = (other-this).norm

  /** @return the snake/manhattan norm of this vector */
  def snakeNorm: Int = abs(x) + abs(y)

  /** @return the snake/manhattan distance of this vector */
  def snakeDist(other: Vec2i): Int = abs(x-other.x) + abs(y-other.y)

  override def toString(): String = s"($x, $y)"
}

object Vec2i {
  def random(minInclusive: Vec2i, maxExclusive: Vec2i) = {
    val x = Random.between(minInclusive.x, maxExclusive.x)
    val y = Random.between(minInclusive.y, maxExclusive.y)
    new Vec2i(x, y)
  }

  val Zero = Vec2i(0,0)
  val Right = Vec2i(1,0)
  val Left = Vec2i(-1,0)
  val Up = Vec2i(0,-1)
  val Down = Vec2i(0,1)
}
