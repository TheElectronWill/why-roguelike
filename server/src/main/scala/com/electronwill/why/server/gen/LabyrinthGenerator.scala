package com.electronwill.why
package server.gen

import util._
import gametype._
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
    val tiles = Grid[Tile](width, height, Tiles.Void.make())
    val entities = Grid[Entity](width, height, null)
    labyrinth(bounds, tiles)
    tiles(end) = Tiles.Stairs.make()
    DungeonLevel(level, s"level $level", start, end, tiles, entities)
  
  private def labyrinth(box: Box, tiles: Grid[Tile]): Unit =
    val center = box.roundedCenter
    if box.width == 3
      val yDoor = Random.between(box.yMin, box.yMax)
      for y <- box.yMin to box.yMax if y != yDoor do
        tiles(center.x, y) = Tiles.Wall.make()
    
    else if box.height == 3
      val xDoor = Random.between(box.xMin, box.xMax)
      for x <- box.xMin to box.xMax if x != xDoor do
        tiles(x, center.y) = Tiles.Wall.make()
    
    else if box.width > 3 || box.height > 3 // ignore box if too small
      val (subA, subB) = box.split()
      labyrinth(subA, tiles)
      labyrinth(subB, tiles)
}
