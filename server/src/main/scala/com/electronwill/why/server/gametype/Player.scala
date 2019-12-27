package com.electronwill.why
package server.gametype

import gametype.Entity
import gametype.EntityId
import server.network.WhyClientAttach

class Player(val client: WhyClientAttach) extends ServerEntity(s"player ${client.clientId}") {
  def tpe = Entities.Player
}
