package com.electronwill.why

trait Tile {
  val name: String

  val id: Int

  /** @return how to display the tile */
  def char: Char

  /** @return true if it blocks the entities, false otherwise */
  def isBlock: Boolean
}

class BasicTile(
    val name: String,
    val char: Char,
    val isBlock: Boolean = false,
    val id: Int = -1 // TODO
  ) extends Tile
