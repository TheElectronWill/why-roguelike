package com.electronwill.why
package client.gametype

import gametype._
import ansi.ColorSetting

/** A client-side Tile that holds the data controlled by the server. */
final class ClientTile(var tpe: TileTypeData) extends Tile {
  var name: String = tpe.name
  var character: Char = tpe.defaultChar
  var customColor = ColorSetting(None, None)
  var isBlock: Boolean = tpe.isBlock
}
