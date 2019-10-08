package com.electronwill.why.server

class ServerDungeonLevel(val level: Int, val name: String, val terrain: TerrainStorage)
extends DungeonLevel {
  def entityAt(pos: Vec2i): Option[Entity] = None // TODO implement entities
}

class TerrainStorage(val width: Int, val height: Int) extends DungeonTerrain {
  private val tiles = Array[Tile](height*width)
  private inline def idx(pos: Vec2i): Int = pos.y*width + pos.x

  def apply(pos: Vec2i): Tile = tiles(idx(pos))
  def update(pos: Vec2i, t: Tile): Unit = tiles(idx(pos)) = t
}
