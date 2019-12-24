package com.electronwill.why
package server.gametype

import gametype.Entity
import gametype.EntityId
import server.WhyClientAttach

class Player(val client: WhyClientAttach) extends ServerEntity(s"player ${client.clientId}", '@')
