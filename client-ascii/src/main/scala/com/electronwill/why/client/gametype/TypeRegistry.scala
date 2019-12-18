package com.electronwill.why
package client.gametype

import scala.collection.mutable.LongMap

abstract class TypeRegistry[T] {
  private val idMap = LongMap[T]()

  final def typeWithId(id: Int): T = idMap(id.toLong)

  final def register(id: Int, t: T): Unit = idMap(id.toLong) = t
}
