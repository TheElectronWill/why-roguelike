package com.electronwill.why
package gametype

import util.Vec2i

/** A game entity. */
trait Entity {
  def position: Vec2i

  def name: String
  def character: Char
  def id: Int
}

/** @return the EntityType associated with this Entity. */
def [A <: Entity](a: A) tpe: EntityType[A] = EntityType.typeOf(a.getClass).asInstanceOf[EntityType[A]]

abstract class EntityBase(val character: Char) extends Entity {
  var position: Vec2i = Vec2i.Zero
  var name: String = toString

  def id: Int = hashCode
}
