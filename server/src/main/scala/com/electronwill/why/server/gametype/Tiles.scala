package com.electronwill.why
package server.gametype

import gametype._
import TileType.register

case class BasicTile(name: String, character: Char) extends Tile {
  def isBlock = false
}

case class BlockTile(name: String, character: Char) extends Tile {
  def isBlock = true
}

/** Standard tile types. */
object Tiles {
  object VoidTile extends BasicTile("void", ' ')
  object UnknownTile extends BasicTile("unknown", '?')
  object FloorTile extends BasicTile("floor", ' ')
  object WallTile extends BlockTile("wall", '=')
  object StairsTile extends BasicTile("stairs", 'x')

  val Void = register(VoidTile)
  val Unknown = register(UnknownTile)
  val Floor = register(FloorTile)
  val Wall = register(WallTile)
  val Stairs = register(StairsTile)
}
