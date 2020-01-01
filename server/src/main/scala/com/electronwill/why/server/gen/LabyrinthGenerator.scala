package com.electronwill.why
package server.gen

import gametype._
import geom._
import server.ServerDungeonLevel
import server.gametype._

import scala.util.Random

class LabyrinthGenerator(private val width: Int,
                         private val height: Int,
                         private val minSplits: Int)
    extends LevelGenerator {

  def generate(level: Int) =
    val bounds = Box.positive(width, height)
    val start = bounds.randomBorderPoint
    val end =
      var p = bounds.randomBorderPoint
      while start == p do
        p = bounds.randomBorderPoint
      p
    val tiles = Grid[RegisteredType](width, height, Tiles.Void)
    labyrinth(bounds, tiles)
    tiles(end) = Tiles.Stairs
    ServerDungeonLevel(level, s"level $level", start, end, tiles)

  private def labyrinth(box: Box, tiles: Grid[RegisteredType]): Unit =
    val center = box.roundedCenter
    if box.width == 3
      val yDoor = Random.between(box.yMin, box.yMax+1)
      for y <- box.yMin to box.yMax if y != yDoor do
        tiles(center.x, y) = Tiles.Wall

    else if box.height == 3
      val xDoor = Random.between(box.xMin, box.xMax+1)
      for x <- box.xMin to box.xMax if x != xDoor do
        tiles(x, center.y) = Tiles.Wall

    else if box.width > 3 || box.height > 3 // ignore box if too small
      val (subA, subB) = box.split()
      labyrinth(subA, tiles)
      labyrinth(subB, tiles)
}
