package com.electronwill.why
package gametype

/** A game entity. */
trait Entity {
  def position: Vec2i

  def name: String
  def character: Char
  def id: EntityId
}
