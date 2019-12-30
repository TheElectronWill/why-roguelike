package com.electronwill.why

import scala.math._
import scala.util.Random

/** Vector of 2 integer coordinates (immutable). */
final case class Vec2i(x: Int, y: Int) {
  def +(v: Vec2i) = Vec2i(x+v.x, y+v.y)
  def -(v: Vec2i) = Vec2i(x-v.x, y-v.y)
  def *(k: Int) = Vec2i(k*x, k*y)
  def /(k: Int) = Vec2i(x/k, y/k)
  def unary_- = Vec2i(-x, -y)

  def addX(x: Int) = Vec2i(this.x + x, y)
  def addY(y: Int) = Vec2i(x, this.y + y)

  def coordInDirection(dir: Direction): Int = if dir.isVertical then y else x

  def isNotAfter(other: Vec2i, dir: Direction): Boolean = dir match
    case Direction.UP    => y >= other.y
    case Direction.DOWN  => y <= other.y
    case Direction.RIGHT => x <= other.x
    case Direction.LEFT  => x >= other.x

  def isBefore(other: Vec2i, dir: Direction): Boolean = dir match
    case Direction.UP    => y > other.y
    case Direction.DOWN  => y < other.y
    case Direction.RIGHT => x < other.x
    case Direction.LEFT  => x > other.x

  def squaredNorm: Int = x*x + y*y

  def norm: Double = sqrt(squaredNorm.toDouble)

  def squaredDist(other: Vec2i): Int = (other-this).squaredNorm

  def dist(other: Vec2i): Double = (other-this).norm

  /** @return the snake/manhattan norm of this vector */
  def snakeNorm: Int = abs(x) + abs(y)

  /** @return the snake/manhattan distance of this vector */
  def snakeDist(other: Vec2i): Int = abs(x-other.x) + abs(y-other.y)

  def toFloating: Vec2f = Vec2f(x, y)

  override def toString(): String = s"($x, $y)"
}

object Vec2i {
  def random(minInclusive: Vec2i, maxExclusive: Vec2i) = {
    val x = Random.between(minInclusive.x, maxExclusive.x)
    val y = Random.between(minInclusive.y, maxExclusive.y)
    new Vec2i(x, y)
  }

  def rounded(x: Double, y: Double) = Vec2i(x.round.toInt, y.round.toInt)

  val ZERO = Vec2i(0,0)
  val RIGHT = Vec2i(1,0)
  val LEFT = Vec2i(-1,0)
  val UP = Vec2i(0,-1)
  val DOWN = Vec2i(0,1)
}
