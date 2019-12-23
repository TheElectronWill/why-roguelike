package com.electronwill.why
package server.gametype

import gametype.EntityId
import server.WhyClientAttach

class Player(val client: WhyClientAttach) extends EntityBase('@') {
  var level: DungeonLevel = _
  override def id = EntityId(client.clientId)
}
