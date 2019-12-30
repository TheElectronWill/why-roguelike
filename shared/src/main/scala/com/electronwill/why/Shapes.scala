package com.electronwill.why

import math.{cos, sin, Pi}

/** Generates 2D shapes. Remember that the Y axis points downwards. */
object Shapes {
  private inline val CIRCLE_QUARTER_STEPS = 16
  private inline val STEP_ANGLE = (Pi / 2.0) / CIRCLE_QUARTER_STEPS

  // --- CIRCLES AND DISKS ---
  def circlePoint(center: Vec2i, radius: Int, angle: Double): Vec2i =
    Vec2i.rounded(center.x + radius*cos(angle), center.y + radius*sin(angle))

  /** Generates the bottom-right 1/4 of a circle. */
  def circleQuarter(center: Vec2i, radius: Int): Seq[Vec2i] =
    (0 to CIRCLE_QUARTER_STEPS).map(t => circlePoint(center, radius, STEP_ANGLE * t)).distinct

  /** Generates the bottom half of a circle. */
  def circleHalf(center: Vec2i, radius: Int): Seq[Vec2i] =
    val right = circleQuarter(center, radius)
    val left = right.map(p => Vec2i(-p.x + 2*center.x, p.y))
    left ++ right

  /** Generates a circle (approximate because of integer coordinates). */
  def circle(center: Vec2i, radius: Int): Seq[Vec2i] =
    val down = circleHalf(center, radius)
    val up = down.map(p => Vec2i(p.x, -p.y + 2*center.y))
    up ++ down

  /** Generates the bottom-right 1/4 of a disk (plain circle). Includes the center. */
  def diskQuarter(center: Vec2i, radius: Int): Seq[Vec2i] =
    (0 to radius).flatMap(r => circleQuarter(center, r)).distinct

  /** Generates the bottom half of a disk. Contains the center only once. */
  def diskHalf(center: Vec2i, radius: Int): Seq[Vec2i] =
    val right = diskQuarter(center, radius) // contains the center
    val left = right.collect{case p if p != center => Vec2i(-p.x + 2*center.x, p.y)} // doesn't contain the center
    left ++ right

  /** Generates a disk (approximate because of integer coordinates). Contains the center only once. */
  def disk(center: Vec2i, radius: Int): Seq[Vec2i] =
    val down = diskHalf(center, radius)
    val up = down.collect{case p if p != center => Vec2i(p.x, -p.y + 2*center.y)}
    up ++ down

  // --- TRIANGLES ---
  /** Generates an isoceles triangle (approximate because of integer coordinates).
    * @param top vertice at the top of the triangle
    * @param height height of the triangle
    * @param dir where to create the height
    */
  def isoTriangle(top: Vec2i, height: Int, dir: Direction): Seq[Vec2i] =
    (0 to height).flatMap { h =>
      val a = dir.right.vector*h
      Seq(a, -a).distinct.map(top + dir.vector*h + _)
    }

  /** Generates a plain isoceles triangle (approximate because of integer coordinates).
    * @param top vertice at the top of the triangle
    * @param height height of the triangle
    * @param dir where to create the height
    */
  def plainIsoTriangle(top: Vec2i, height: Int, dir: Direction): Seq[Vec2i] =
    for
      h <- 0 to height // from base to top
      perp <- -h to h // perpendicular to the height of the triangle
    yield
      top + dir.vector*h + dir.right.vector*perp

  // --- RECTANGLES ---
  /** Generates a rectangle. */
  def rect(box: Box): Seq[Vec2i] =
    (box.xMin to box.xMax).flatMap(x => Seq(Vec2i(x,box.yMin), Vec2i(x,box.yMax))) ++
    (box.yMin+1 until box.yMax-1).flatMap(y => Seq(Vec2i(box.xMin,y), Vec2i(box.xMax,y)))

  /** Generates a plain rectangle. */
  def plainRect(box: Box): Seq[Vec2i] =
    for
      x <- box.xMin to box.xMax
      y <- box.yMin to box.yMax
    yield
      Vec2i(x, y)

  /** Generates a plain square of odd length. */
  def plainSquare(center: Vec2i, radius: Int): Seq[Vec2i] =
    for
      x <- center.x-radius to center.x+radius
      y <- center.y-radius to center.y+radius
    yield
      Vec2i(x, y)
}
