package com.electronwill.why

object Tiles {
  val Void = BasicTile("ø", false)
  val Unknown = BasicTile("?", false)
  val Floor = BasicTile("floor", false)
  val Wall = BasicTile("wall", true)
  val StairsDown = BasicTile("stairs-down", true)
}
