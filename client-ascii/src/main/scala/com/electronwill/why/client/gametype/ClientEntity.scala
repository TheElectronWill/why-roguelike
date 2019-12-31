package com.electronwill
package why.client.gametype

import why.gametype._
import why.client.ClientDungeonLevel

/** A client-side entity that holds the data controlled by the server.
  * As opposed to the server side, on the client side there is only one type of entity.
  */
class ClientEntity(id: EntityId, override val tpe: RegisteredType)
    extends Entity(tpe.name) {

  this._id = id
  def id_=(newId: EntityId): Unit = _id = newId

  private var _level: ClientDungeonLevel = null
  override def level: ClientDungeonLevel = _level
  def level_=(l: ClientDungeonLevel) = _level = l
}
