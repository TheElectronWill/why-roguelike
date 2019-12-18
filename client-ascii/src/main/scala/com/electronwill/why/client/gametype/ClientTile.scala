package com.electronwill.why
package client.gametype

import gametype.Tile

/** A client-side Tile that holds the data controlled by the server. */ 
final class ClientTile(val id: Int, val tpe: TileType) extends Tile {
  var name: String = tpe.name
  var character: Char = tpe.defaultChar
  var isBlock: Boolean = tpe.isBlock
}
