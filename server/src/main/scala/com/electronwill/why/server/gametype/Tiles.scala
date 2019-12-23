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
  val Void    = register(BasicTile("void", 'ø'))
  val Unknown = register(BasicTile("?", '?'))
  val Floor   = register(BasicTile("floor", ' '))
  val Wall    = register(BlockTile("wall", '='))
  val Stairs  = register(BasicTile("stairs", 'x'))
}
