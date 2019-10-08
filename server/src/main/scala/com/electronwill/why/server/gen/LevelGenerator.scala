package com.electronwill.why.server

trait LevelGenerator {
  def generate(level: Int): DungeonLevel
}
