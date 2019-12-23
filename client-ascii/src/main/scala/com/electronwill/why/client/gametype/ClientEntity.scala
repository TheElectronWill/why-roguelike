package com.electronwill.why
package client.gametype

import gametype._

/** A client-side entity that holds the data controlled by the server.
  * As opposed to the server side, on the client side there is only one type of entity.
  */
final class ClientEntity(val id: EntityId, val tpe: EntityTypeData, spawnPosition: Vec2i) extends Entity {
  var name: String = tpe.name
  var character: Char = tpe.defaultChar
  var position: Vec2i = spawnPosition
}
