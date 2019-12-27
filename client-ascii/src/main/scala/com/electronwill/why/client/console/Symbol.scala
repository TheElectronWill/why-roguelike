package com.electronwill
package why.client
package console

import gametype.Tiles
import why.gametype.{Entity, RegisteredType => T}

object Symbol {
  private val Wall = Tiles.get("wall")

  def of(tile: T, surrounding: (T, T, T, T)): Char =
    if tile == Wall
      surrounding match
        case (Wall, Wall, Wall, Wall) => '╬'
        case (Wall, Wall, Wall, _) => '╠'
        case (Wall, Wall, _, Wall) => '╩'
        case (Wall, _, Wall, Wall) => '╣'
        case (_, Wall, Wall, Wall) => '╦'
        case (Wall, Wall, _, _)    => '╚'
        case (Wall, _, _, Wall)    => '╝'
        case (_, Wall, Wall, _)    => '╔'
        case (_, _, Wall, Wall)    => '╗'
        case (Wall, _, _, _) | (_, _, Wall, _) => '║' // includes "up and down" (Wall, _, Wall, _)
        case (_, Wall, _, _) | (_, _, _, Wall) => '═' // includes "right and left" (_, Wall, _, Wall)
        case (_, _, _, _) => '▣'
    else
      tile.character

  def of(e: Entity): Char = e.tpe.character
}
