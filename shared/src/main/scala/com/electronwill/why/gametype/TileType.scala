package com.electronwill.why.gametype

final class TileType[+T <: Tile](build: () => T, id: Int) extends RegisteredType[T](build, id)

object TileType extends TypeRegistry[Tile, TileType] {
  override protected def newType[E <: Tile](provider: () => E, id: Int) = TileType(provider, id)
}
