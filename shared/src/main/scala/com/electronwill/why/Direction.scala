package com.electronwill.why

enum Direction(val vector: Vec2i) extends java.lang.Enum[Direction] {
  case UP extends Direction(Vec2i.UP)
  case RIGHT extends Direction(Vec2i.RIGHT)
  case DOWN extends Direction(Vec2i.DOWN)
  case LEFT extends Direction(Vec2i.LEFT)

  def isVertical = this == UP || this == DOWN
  def isHorizontal = !isVertical

  def isPositive = this == RIGHT || this == DOWN
  def isNegative = !isPositive
}

given (d: Direction) extended with {
  def opposite: Direction = d match
    case Direction.UP    => Direction.DOWN
    case Direction.RIGHT => Direction.LEFT
    case Direction.DOWN  => Direction.UP
    case Direction.LEFT  => Direction.RIGHT

  def right: Direction = d match
    case Direction.UP    => Direction.RIGHT
    case Direction.RIGHT => Direction.DOWN
    case Direction.DOWN  => Direction.LEFT
    case Direction.LEFT  => Direction.UP

  def left: Direction = d match
    case Direction.UP    => Direction.LEFT
    case Direction.LEFT  => Direction.DOWN
    case Direction.DOWN  => Direction.RIGHT
    case Direction.RIGHT => Direction.UP
}
