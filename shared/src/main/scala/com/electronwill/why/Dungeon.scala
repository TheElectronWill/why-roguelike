package com.electronwill.why

class DungeonLevel(val level: Int,
                   val name: String,
                   val spawnPosition: Vec2i,
                   val terrain: DungeonTerrain) {
  def entityAt(pos: Vec2i): Option[Entity] = None // TODO implement entities
}

class DungeonTerrain(width: Int, height: Int) {
  private val grid = Grid[Tile](width, height)

  export grid._ // expose grid methods as DungeonTerrain's
}
