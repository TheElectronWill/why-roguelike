package com.electronwill.why
package server.gametype

import gametype._
import util.Vec2i
import TileType.register

/** @return the EntityType associated with this Entity. */
def [A <: Entity](a: A) tpe: EntityType[A] = EntityType.typeOf(a.getClass).asInstanceOf[EntityType[A]]

abstract class EntityBase(val character: Char) extends Entity {
  var position: Vec2i = Vec2i.Zero
  var name: String = toString

  def id: Int = hashCode
}

object Entities {
  // BACKLOG: register some entities
}
