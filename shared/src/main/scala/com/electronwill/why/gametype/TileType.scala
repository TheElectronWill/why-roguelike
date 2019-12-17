package com.electronwill.why.gametype

final class TileType[+T <: Tile](make: () => T, id: Int) extends RegisteredType[T](make, id)

object TileType extends TypeRegistry[Tile, TileType] {
  override protected def makeType[E <: Tile](f: () => E, id: Int) = TileType(f, id)
}
