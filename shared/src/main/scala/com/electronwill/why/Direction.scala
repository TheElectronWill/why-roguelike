package com.electronwill.why

enum Direction(val vector: Vec2i) extends java.lang.Enum[Direction] {
  case UP extends Direction(Vec2i.UP)
  case RIGHT extends Direction(Vec2i.RIGHT)
  case DOWN extends Direction(Vec2i.DOWN)
  case LEFT extends Direction(Vec2i.LEFT)
}

def (d: Direction) opposite: Direction = d match
  case Direction.UP    => Direction.DOWN
  case Direction.RIGHT => Direction.LEFT
  case Direction.DOWN  => Direction.UP
  case Direction.LEFT  => Direction.RIGHT
