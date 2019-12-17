package com.electronwill.why.gametype

import scala.reflect.ClassTag

final class EntityType[+E <: Entity](make: () => E, id: Int) extends RegisteredType[E](make, id)

object EntityType extends TypeRegistry[Entity, EntityType] {
  override protected def makeType[E <: Entity](f: () => E, id: Int) = EntityType(f, id)
}
