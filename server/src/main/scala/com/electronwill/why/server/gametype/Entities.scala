package com.electronwill.why
package server.gametype

import scala.reflect.ClassTag

object Entities extends gametype.TypeRegistry {
  val Player = register("player", '@', true)
}
