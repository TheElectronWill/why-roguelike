package com.electronwill.why
package gametype

import util.Vec2i

/** A game entity. */
trait Entity {
  def position: Vec2i

  def name: String
  def character: Char  
}

def [A <: Entity](a: A) tpe: EntityType[A] = EntityType.typeOf(a.getClass).asInstanceOf[EntityType[A]]
