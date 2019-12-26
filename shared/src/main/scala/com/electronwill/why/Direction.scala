package com.electronwill.why

enum Direction(val vector: Vec2i) {
  case UP extends Direction(Vec2i.UP)
  case RIGHT extends Direction(Vec2i.RIGHT)
  case DOWN extends Direction(Vec2i.DOWN)
  case LEFT extends Direction(Vec2i.LEFT)
}
