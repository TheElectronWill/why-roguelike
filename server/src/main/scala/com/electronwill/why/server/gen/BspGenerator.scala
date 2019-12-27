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
    //val (width, height) = terrainSize(level)
    val (width, height) = terrainSize(level)
    val tiles = Grid[RegisteredType](width, height, Tiles.Void)

    // 2: divide the space
    val tree = BspTree(Box.corners(Vec2i(1,1), Vec2i(width-1, height-1))) // prevent the floor from touching the terrain's borders
    split(tree.root, 0, splitCount(level))

    // 3: create the rooms (and the spawn and exit in 2 different rooms)
    var spawn = Vec2i.ZERO
    var lastRoom: Box = null
    for node <- tree do
      if node.isLeaf
        val room = makeRoom(node, tiles)
        if spawn == Vec2i.ZERO
          spawn = room.randomPoint
        lastRoom = room

    val exit = lastRoom.randomPoint
    tiles(exit) = Tiles.Stairs

    // 4: connect the rooms
    connectChilds(tree.root, tiles)

    // 5: build the walls
    // We do this after creating the rooms to allow merges
    val wallsPositions = SimpleBag[Vec2i]()
    for
      x <- 0 until width
      y <- 0 until height
    do
      val pos = Vec2i(x, y)
      if tiles(pos) == Tiles.Void && tiles.squareAround(pos, 1).exists(_ != Tiles.Void)
        wallsPositions += pos

    wallsPositions.foreach(tiles(_) = Tiles.Wall)

    // Done!
    ServerDungeonLevel(level, s"level $level", spawn, exit, tiles)

  private def connectChilds(n: BspNode, tiles: Grid[RegisteredType]): Unit =
    if n.isLeaf then return
    val (a,b) =
      if n.childA.isLeaf
        // We can introduce some randomness when connecting two leafs
        (n.childA.box.randomPoint, n.childB.box.randomPoint)
      else
        // We CANNOT choose the points randomly when connecting two higher-level nodes,
        // because otherwise we have no guarantee that there is a room containing the points.
        (n.childA.box.roundedCenter, n.childB.box.roundedCenter)
    makeCorridor(a, b, tiles)
    connectChilds(n.childA, tiles)
    connectChilds(n.childB, tiles)

  private def makeCorridor(a: Vec2i, b: Vec2i, tiles: Grid[RegisteredType]) =
    val diff = b-a
    Logger.info(s"Connecting $a and $b")
    // vertical (y) part
    if diff.y != 0
      for y <- a.y to b.y by diff.y.sign do
        tiles(a.x, y) = Tiles.Floor

    // horizontal (x) part
    if diff.x != 0
      for x <- a.x to b.x by diff.x.sign do
        tiles(x, b.y) = Tiles.Floor

  /** Creates a room of random size inside of the given box. */
  private def makeRoom(node: BspNode, tiles: Grid[RegisteredType]) =
    val w = Random.between(2, node.box.width+1)
    val h = Random.between(2, node.box.height+1)
    val room = Box.center(node.box.roundedCenter, w, h)
    Logger.info(s"Generating room between ${room.cornerMin} and ${room.cornerMax}")
    for
      x <- room.xMin to room.xMax
      y <- room.yMin to room.yMax
    do
      tiles(x, y) = Tiles.Floor

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
