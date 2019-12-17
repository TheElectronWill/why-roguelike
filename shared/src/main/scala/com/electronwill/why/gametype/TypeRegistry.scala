package com.electronwill.why.gametype

import scala.collection.mutable.{HashMap, LongMap}
import scala.reflect.ClassTag

/** A registry for game types. 
 * 
 * @author (c) TheElectronWill and the Tuubes project (https://github.com/tuubes/TuubesCore/pull/59)
 * @tparam A the type of things to register, for instance `Block`
 * @tparam T a class that "materializes" the game type of those things,
             for instance if A is `Block` then T should be `BlockType`
 */
abstract class TypeRegistry[A, T[+_<:A] <: RegisteredType[A]] {
  private val classMap = HashMap[Class[?], T[A]]() // runtime class -> type
  private val idMap = LongMap[T[A]]() // id -> type
  private var lastId = -1

  /** Registers a simple type of "thing" that has no state. */
  def register[B <: A](singleton: B): T[B] =
    val provider = ()=>singleton
    val runtimeClass = singleton.getClass
    val tag = ClassTag[A](runtimeClass)
    classMap.getOrElseUpdate(runtimeClass, internalNewType(provider, tag)).asInstanceOf[T[B]]

  /** Registers an advanced type of "thing" that may have an internal state ("stateful").
   *  It is guaranteed that each thing of that type will be a separate instance.
   *  Each instance stores its own data independently.
   */
  def registerStateful[B <: A](builder: () => B)(given tag: ClassTag[B]): T[B] =
    classMap.getOrElseUpdate(tag.runtimeClass, internalNewType(builder, tag)).asInstanceOf[T[B]]

  /** Creates a "thing" of some precisely known type.
   *
   * @param B the type of thing to create
   * @param tag tag a `ClassTag` that contains information about the class `B`
                (automatically provided by the compiler)
     @return a new thing of type `B`
   */
  def create[B <: A](given tag: ClassTag[B]): B =
    classMap(tag.runtimeClass).asInstanceOf[RegisteredType[B]].build()

  /** Creates a "thing" of some less precise type.
   *
   * @param tpe the type of thing to create
   * @return a new thing
   */
  def create[B <: A](tpe: T[B]): B = tpe.build().asInstanceOf[B]

  /** Materializes a type. */
  def typeOf[B <: A](given tag: ClassTag[B]): T[B] = classMap(tag.runtimeClass).asInstanceOf[T[B]]

  /** Materializes a type. */
  def typeOf(runtimeClass: Class[_<:A]): T[A] = classMap(runtimeClass).asInstanceOf[T[A]]

  /** Searches a type by internal id. */
  def typeWithId(id: Int): T[A] = idMap(id.toLong).asInstanceOf[T[A]]

  // --- Registry Internals ---
  /** Creates a new instance of `T` (a "type of thing") for a `B <: A` (the "thing" to be typed).
   * 
   * @param provider a function that creates instances of `B` (it provides "things")
   * @param id the internal id to associate to the new instance of `T`
   * @return a new registered type
   */
  protected def newType[B <: A](provider: ()=>B, id: Int): T[B]

  /** Generates a new internal id and registers a new type with that id. */
  protected final def internalNewType[B <: A](provider: ()=>B, tag: ClassTag[B]): T[B] =
    // Generates a new type id:
    val id = lastId
    lastId += 1
    // Registers the type with this id:
    val tpe = newType(provider, id)
    idMap(id.toLong) = tpe
    tpe

}
