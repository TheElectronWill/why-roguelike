package com.electronwill.why.gametype

trait Tile {
  def name: String
  def character: Char

  /** @return true if it blocks the entities, false otherwise */
  def isBlock: Boolean
}

/** @return the TileType associated with this Tile. */
def [A <: Tile](a: A) tpe: TileType[A] = TileType.typeOf(a.getClass).asInstanceOf[TileType[A]]


case class BasicTile(name: String, character: Char) extends Tile {
  def isBlock = false
}

case class BlockTile(name: String, character: Char) extends Tile {
  def isBlock = true
}
