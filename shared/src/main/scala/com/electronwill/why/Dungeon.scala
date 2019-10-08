package com.electronwill.why

trait DungeonLevel {
  val level: Int
  val name: String
  def terrain: DungeonTerrain
  def entityAt(pos: Vec2i): Option[Entity]
}

trait DungeonTerrain {
  def apply(pos: Vec2i): Tile
  def update(pos: Vec2i, t: Tile): Unit
}
