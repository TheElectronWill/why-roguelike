package com.electronwill
package why.server
package gametype

import why.gametype._
import why._

abstract class ServerEntity(name: String) extends Entity(name) {
  private var _level: ServerDungeonLevel = _
  override def level: ServerDungeonLevel = _level
  def level_=(l: ServerDungeonLevel) = _level = l

  def init(level: ServerDungeonLevel, position: Vec2i, id: EntityId): Unit =
    this.level = level
    this.position = position
    this._id = id

  def deinit(): Unit =
    init(null, null, EntityId(-1))

  def isValid: Boolean = (level != null)
}
