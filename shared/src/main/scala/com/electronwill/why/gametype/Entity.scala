package com.electronwill.why
package gametype

import ansi.ColorSetting

/** A game entity. */
abstract class Entity(var name: String) {
  var position: Vec2i = _
  var customColor = ColorSetting(None, None)

  protected var _id: EntityId = EntityId(-1)
  def id: EntityId = _id

  def level: DungeonLevel[?]
  def tpe: RegisteredType
}
