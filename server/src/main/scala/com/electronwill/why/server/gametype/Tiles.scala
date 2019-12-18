package com.electronwill.why
package server.gametype

import gametype._
import TileType.register

/** @return the TileType associated with this Tile. */
def [A <: Tile](a: A) tpe: TileType[A] = TileType.typeOf(a.getClass).asInstanceOf[TileType[A]]


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
