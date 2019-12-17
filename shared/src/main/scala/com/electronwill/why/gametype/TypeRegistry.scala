package com.electronwill.why.gametype

import scala.collection.mutable.{HashMap, LongMap}
import scala.reflect.ClassTag

/** A registry for game types. 
 * 
 * @author (c) TheElectronWill and the Tuubes project (https://github.com/tuubes/TuubesCore/pull/59)
 * @tparam I instance type, e.g. `Tile`
 * @tparam T type type, i.e. a type that represent the idea of "type of I".
 *           For instance, if I is `Tile` then T will be `TileType`.
 * @define iname I
 * @define itype T
 */
abstract class TypeRegistry[I, T <: [+X <: I] =>> RegisteredType[X]] {
  /* --- Type parameters explanation ---
   I is the instance type (more precisely, the supertype of all instances we're interested in).
   
   The syntax [X] =>> Y denotes a type lambda that takes one parameter X and gives Y.
   Here, T must be a higher-kinded type that takes one parameter X, bounded by I,
   and gives a RegisteredType[X].
   That is, for all X such that X <: I, T[X] <: RegisteredType[X].
   Furthermore, X is covariant, meaning that (A <: B) implies (T[A] <: T[B]), which
   is logical for types (notice how RegisteredType is covariant as well).
  */

  // --- Registry Internals ---
  private val classMap = HashMap[Class[?], T[I]]() // runtime class -> type
  private val idMap = LongMap[T[I]]() // id -> type
  private var lastId = -1 // counter

  /** TO IMPLEMENT IN SUBCLASSES: makes an instance of T */
  protected def makeType[Instance <: I](provider: () => Instance, id: Int): T[Instance] // abstract

  /** Generates a new internal id and registers a new type with that id. */
  protected final def makeIdAndType[Instance <: I](provider: () => Instance): T[Instance] =
    // Generates a new type id:
    val id = lastId
    lastId += 1
    // Registers the type with this id:
    val tpe = makeType(provider, id)
    idMap(id.toLong) = tpe
    tpe

  protected final def getOrRegister[Instance <: I](runtimeClass: Class[?], instanceMaker: () => Instance) =
    classMap.getOrElseUpdate(runtimeClass, makeIdAndType(instanceMaker)).asInstanceOf[T[Instance]]
    // in the map we can only store T[I] not T[Instance] because Instance changes for each entry
    // that's why an explicit type cast (.asInstanceOf) is needed
  

  // --- Registry Public API ---
  /** Registers a type of $iname for which there exist only one instance.
    *
    * @param singleton the instance of this type
    * @return a new $itype that represents the type of this instance 
    */
  def register[Instance <: I](singleton: Instance): T[Instance] =
    val runtimeClass: Class[? <: Instance] = singleton.getClass
    getOrRegister(singleton.getClass, () => singleton)
  
  /** Registers a type of $iname for which there can be more than one instance.
    *
    * @param instanceMaker the function that construct new instances of this type
    * @param tag given (implicit) compile-time information about the class of those instances
    * @return a new $itype that represents this type of I
    */
  def register[Instance <: I](instanceMaker: () => Instance)(given tag: ClassTag[Instance]): T[Instance] =
    val runtimeClass: Class[?] = tag.runtimeClass // Note that runtimeClass doesn't provide any bound
    getOrRegister(runtimeClass, instanceMaker)
  
  /** Gets an instance of $iname, given we know its precise type at compile time. */
  def create[Instance <: I](given tag: ClassTag[Instance]): Instance =
    classMap(tag.runtimeClass).asInstanceOf[T[Instance]].build()
  
  /** Gets an instance of $iname, given its runtime class. */
  def create(runtimeClass: Class[? <: I]): I =
    classMap(runtimeClass).build()
  
  /** Gets a $itype, given we know its precise type at compile time. */
  def typeOf[Instance <: I](given tag: ClassTag[Instance]): T[Instance] =
    classMap(tag.runtimeClass).asInstanceOf[T[Instance]]
  
  /** Gets a $itype, given its runtime class. */
  def typeOf(runtimeClass: Class[? <: I]): T[I] =
    classMap(runtimeClass)
  
  /** Gets a $itype, given its id. */
  def typeWithId(id: Int): T[I] = idMap(id.toLong)
}
