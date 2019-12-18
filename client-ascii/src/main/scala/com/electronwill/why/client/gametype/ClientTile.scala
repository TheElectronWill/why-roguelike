package com.electronwill.why
package client.gametype

import gametype._

/** A client-side Tile that holds the data controlled by the server. */ 
final class ClientTile(val id: Int, val tpe: TileTypeData) extends Tile {
  var name: String = tpe.name
  var character: Char = tpe.defaultChar
  var isBlock: Boolean = tpe.isBlock
}
