package com.electronwill.why

import util._
import gametype._

class DungeonLevel(val level: Int,
                   val name: String,
                   val spawnPosition: Vec2i,
                   val exitPosition: Vec2i,
                   val terrain: Grid[Tile]) {
  def entityAt(pos: Vec2i): Option[Entity] = None // TODO implement entities
}
