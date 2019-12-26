package com.electronwill.why
package server.gametype

object Tiles extends gametype.TypeRegistry {
  val Void    = register("void", ' ', true)
  val Unknown = register("unknown", '?', true)
  val Floor   = register("floor", ' ', false)
  val Wall    = register("wall", '=', true)
  val Stairs  = register("stairs", 'x', false)
}
