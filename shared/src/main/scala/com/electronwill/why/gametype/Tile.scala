package com.electronwill.why.gametype

trait Tile {
  def name: String
  def character: Char

  /** @return true if it blocks the entities, false otherwise */
  def isBlock: Boolean
}
