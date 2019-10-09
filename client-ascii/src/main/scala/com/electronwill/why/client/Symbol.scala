package com.elecronwill.why.client

object Symbol {
  def apply(t: Tile): Char = t match {
    case Nothing    => 'ø'
    case Unknown    => '?'
    case Floor      => ' '
    case Wall       => '.'
    case StairsDown => 'x'
  }

  def apply(e: Entity): Char = e match {
    //case Player     => '@'
    case _          => '#'
  }
}
