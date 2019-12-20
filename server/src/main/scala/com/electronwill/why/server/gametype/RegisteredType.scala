package com.electronwill.why
package server.gametype

/** A type of "thing" that has been registered to a registry
  * @tparam I the type of "things" instances
  * @param make function that creates instances of "thing"
  * @param id the type id
  */
abstract class RegisteredType[+I](val make: () => I, val id: Int)

import util._
def [I](grid: Grid[I]) update(pos: Vec2i, tpe: RegisteredType[I]): Unit = grid.update(pos, tpe.make())
def [I](grid: Grid[I]) update(x: Int, y: Int, tpe: RegisteredType[I]): Unit = grid.update(x, y, tpe.make())

