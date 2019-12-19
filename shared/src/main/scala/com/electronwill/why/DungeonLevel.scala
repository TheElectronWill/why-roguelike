package com.electronwill.why

import util._
import gametype._

class DungeonLevel(val level: Int,
                   val name: String,
                   val spawnPosition: Vec2i,
                   val exitPosition: Vec2i,
                   val terrain: Grid[Tile],
                   val entities: Grid[Entity]) {
}
