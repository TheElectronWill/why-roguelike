package com.electronwill.why.gametype

import scala.reflect.ClassTag

final class EntityType[+E <: Entity](build: () => E, id: Int) extends RegisteredType[E](build, id)

object EntityType extends TypeRegistry[Entity, EntityType] {
  override protected def newType[E <: Entity](provider: () => E, id: Int) = EntityType(provider, id)
}
