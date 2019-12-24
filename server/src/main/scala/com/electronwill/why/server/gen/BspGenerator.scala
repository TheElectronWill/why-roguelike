package com.electronwill
package why.server.gen

import collection.SimpleBag
import why._
import why.gametype._
import why.server.gametype._
import why.server.ServerDungeonLevel

import scala.util.Random

/**
 * Un générateur de niveau basé sur un arbre binaire de partitionnement
 * de l'espace (Binary Space Partitioning Tree, abrégé "BSP Tree").
 *
 * L'idée est de diviser l'espace en deux de façon récursive et de créer
 * une salle dans chaque feuille de l'arbre. Les salles qui ont le même
 * noeud parent sont ensuite reliées par un chemin.
 */
class BspGenerator(private val minWidth: Int,
                   private val minHeight: Int,
                   private val maxWidth: Int,
                   private val maxHeight: Int,
                   private val minSplits: Int)
  extends LevelGenerator {

  def generate(level: Int): ServerDungeonLevel =
    // 1: prepare the space
    val (width, height) = terrainSize(level)
    val tiles = Grid[Tile](width, height, Tiles.Void.make())

    // 2: divide the space
    val tree = BspTree(Box.positive(width, height))
    split(tree.root, 0, splitCount(level))

    // 3: create the rooms (and the spawn and exit in 2 different rooms)
    var spawn = Vec2i.Zero
    var lastRoom: Box = null
    for node <- tree do
      if node.isLeaf
        val room = makeRoom(node, tiles)
        if spawn == Vec2i.Zero
          spawn = room.randomPoint
        lastRoom = room

    val exit = lastRoom.randomPoint
    tiles(exit) = Tiles.Stairs.make()

    // 4: connect the rooms
    connectChilds(tree.root, tiles)

    // 5: build the walls
    for
      x <- 0 to width
      y <- 0 to height
    do
      if tiles.around(x, y).tHas(_.tpe != Tiles.Void)
        tiles(x, y) = Tiles.Wall.make()

    // Done!
    ServerDungeonLevel(level, s"level $level", spawn, exit, tiles)

  private def connectChilds(n: BspNode, tiles: Grid[Tile]): Unit =
    if n.isLeaf then return
    val a = n.childA.box.randomPoint
    val b = n.childB.box.randomPoint
    makeCorridor(a, b, tiles)

  private def makeCorridor(a: Vec2i, b: Vec2i, tiles: Grid[Tile]) =
    val diff = b-a
    // horizontal (x) part
    for x <- a.x to b.x by diff.x.sign do
      tiles(x, a.y) = Tiles.Floor.make()

    // vertical (y) part
    for y <- a.y to b.y by diff.y.sign do
      tiles(b.x, y) = Tiles.Floor.make()

  /** Creates a room of random size inside of the given box. */
  private def makeRoom(node: BspNode, tiles: Grid[Tile]) =
    val w = Random.between(2, node.box.width)
    val h = Random.between(2, node.box.height)
    val room = Box.center(node.box.roundedCenter, w, h)
    for
      x <- room.xMin to room.xMax
      y <- room.yMin to room.yMax
    do
      tiles(x, y) = Tiles.Floor.make()

    node.box = room
    room

  /** Computes how many splitting operations to do */
  private def splitCount(level: Int) = math.max(minSplits, level/2)

  /** Computes the width and height of the terrain. level++ implies terrain++ */
  private def terrainSize(level: Int) =
    val w = math.min(maxWidth, minWidth + level)
    val h = math.min(maxHeight, minHeight + level)
    (w, h)

  /** Recursively splits a BSP Node. */
  private def split(n: BspNode, count: Int, maxSplits: Int): Unit =
    if count < maxSplits then
      n.split()
      split(n.childA, count+1, maxSplits)
      split(n.childB, count+1, maxSplits)
    // else stop


}
