package com.electronwill.why
package server.gametype

import gametype._
import scala.reflect.ClassTag

class EntityType[+E <: Entity](val defaultChar: Char, make: () => E, id: Int)
  extends RegisteredType[E](make, id)

object EntityType extends TypeRegistry[Entity, EntityType, Char] {
  override protected def makeType[E <: Entity](c: Char, f: () => E, id: Int) = EntityType(c, f, id)
  override protected def extractInfo(e: Entity) = e.character
}
