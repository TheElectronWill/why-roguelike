package com.electronwill.why.server.gen

import com.electronwill.why.DungeonLevel

trait LevelGenerator {
  def generate(level: Int): DungeonLevel
}
