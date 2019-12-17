package com.electronwill.why.gametype

import TileType.register

/** Standard tile types. */
object Tiles {
  val Void    = register(BasicTile("void", 'ø'))
  val Unknown = register(BasicTile("?", '?'))
  val Floor   = register(BasicTile("floor", ' '))
  val Wall    = register(BlockTile("wall", '='))
  val Stairs  = register(BasicTile("stairs", 'x'))
}
