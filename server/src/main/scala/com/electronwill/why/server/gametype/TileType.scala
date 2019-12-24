package com.electronwill.why
package server.gametype

import gametype._

final class TileType[+T <: Tile](val defaultChar: Char, make: () => T, id: Int)
    extends RegisteredType[T](make, id) {

  override def toString = s"TileType($defaultChar, $id)"
}

object TileType extends TypeRegistry[Tile, TileType, Char] {
  override protected def makeType[E <: Tile](c: Char, f: () => E, id: Int) = TileType(c, f, id)
  override protected def extractInfo(t: Tile) = t.character
}
