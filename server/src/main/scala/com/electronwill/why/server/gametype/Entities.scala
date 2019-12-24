package com.electronwill.why
package server.gametype

import gametype._
import EntityType.register

object Entities {
  // BACKLOG: create more entities

  val Player = register[Player]('@', () => ???)
  // NOTE: Player is a special case, the only purpose of this registration is to register
  // an ID and a Char for the player type. The make() method does NOT work with players.
  // To create an instance of Player, you must use the constructor: Player(clientAttach)
}
