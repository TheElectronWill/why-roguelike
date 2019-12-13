package com.electronwill.why.client

import com.electronwill.why.{Tile, Entity}
import com.electronwill.why.Tiles._

object Symbol {
  def apply(t: Tile, surrounding: (Tile,Tile,Tile,Tile)): Char = t match
    case Void       => 'ø'
    case Unknown    => '?'
    case Floor      => ' '
    case StairsDown => 'x'
    case Wall       => surrounding match
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

  def apply(e: Entity): Char = e match
    case _          => '#'
    // TODO case Player => '@'

}
