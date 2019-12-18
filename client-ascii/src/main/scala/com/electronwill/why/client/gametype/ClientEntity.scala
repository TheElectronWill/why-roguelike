package com.electronwill.why
package client.gametype

import gametype._
import util.Vec2i

/** A client-side entity that holds the data controlled by the server.
  * As opposed to the server side, on the client side there is only one type of entity.
  */
final class ClientEntity(val id: Int, val tpe: EntityTypeData, spawnPosition: Vec2i) extends Entity {
  var name: String = tpe.name
  var character: Char = tpe.defaultChar
  var position: Vec2i = spawnPosition
}
