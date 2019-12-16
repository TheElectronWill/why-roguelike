package com.electronwill
package why

import collection.RecyclingIndex

object Tiles {
  private val index = RecyclingIndex[Tile]()

  private[why] def register(t: Tile): Unit = index += t

  val Void = BasicTile("void", 'ø')
  val Unknown = BasicTile("?", '?')
  val Floor = BasicTile("floor", ' ')
  val Wall = BasicTile("wall", '=', true)
  val StairsDown = BasicTile("stairs-down", 'x')
}
