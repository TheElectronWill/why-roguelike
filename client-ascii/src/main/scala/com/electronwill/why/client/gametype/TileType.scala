package com.electronwill.why.client.gametype

/** A client-side TileType holds the type data controlled by the server. */
final case class TileType(val id: Int, val name: String, val defaultChar: Char, val isBlock: Boolean)

object TileType extends TypeRegistry[TileType]
