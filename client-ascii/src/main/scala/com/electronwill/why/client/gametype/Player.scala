package com.electronwill.why
package client.gametype

import gametype.EntityId

final class Player(id: EntityId, val username: String) extends ClientEntity(id, Entities.get("player")) {

}
