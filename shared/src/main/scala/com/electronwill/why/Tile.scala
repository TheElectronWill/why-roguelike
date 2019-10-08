package com.electronwill.why

trait Tile {
  val name: String

  /** @return true if it blocks the entities, false otherwise */
  def isBlock: Boolean
}

class BasicTile(val name: String, val isBlock: Boolean) extends Tile
