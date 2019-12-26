package com.electronwill.why.gametype

import scala.collection.mutable.{HashMap, LongMap}
import scala.reflect.ClassTag

/** A registry for game types. It can generate a unique id for each type. */
class TypeRegistry extends Iterable[RegisteredType] {
  private val nameMap = HashMap[String, RegisteredType]() // name -> type
  private val idMap = LongMap[RegisteredType]() // id -> type
  private var lastId = -1 // counter

  private def nextId =
    lastId += 1
    lastId

  def register(name: String, character: Char, isBlocking: Boolean): RegisteredType =
    val tpe = RegisteredType(name, nextId, character, isBlocking)
    register(tpe)
    tpe

  def register(tpe: RegisteredType) =
    nameMap(tpe.name) = tpe
    idMap(tpe.id) = tpe

  def get(name: String): RegisteredType = nameMap(name)
  def get(id: Int): RegisteredType = idMap(id)

  override def iterator = idMap.valuesIterator
  override def foreach[U](f: RegisteredType => U) = idMap.foreachValue(f)

  override def hasDefiniteSize = true
  override def size = idMap.size
}
