package com.electronwill.why

import scala.math.sqrt

/** Vector of 2 floating-point coordinates (immutable). */
final case class Vec2f(x: Float, y: Float) {
  def +(v: Vec2f) = Vec2f(x+v.x, y+v.y)
  def -(v: Vec2f) = Vec2f(x-v.x, y-v.y)
  def *(k: Float) = Vec2f(k*x, k*y)
  def /(k: Float) = Vec2f(x/k, y/k)
  def unary_- = Vec2f(-x, -y)

  def squaredNorm: Float = x*x + y*y

  def norm: Float = sqrt(squaredNorm.toDouble).toFloat

  def squaredDist(other: Vec2f): Float = (other-this).squaredNorm

  def dist(other: Vec2f): Float = (other-this).norm

  def rounded: Vec2i = Vec2i(x.toInt, y.toInt)

  override def toString(): String = s"($x, $y)"
}
