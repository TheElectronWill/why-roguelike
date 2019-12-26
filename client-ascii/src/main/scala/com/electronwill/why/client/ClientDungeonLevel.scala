package com.electronwill
package why.client

import collection.RecyclingIndex
import gametype._
import why.{DungeonLevel, Grid, Vec2i}
import why.gametype.{EntityId, RegisteredType}

class ClientDungeonLevel(number: Int, name: String, spawn: Vec2i, exit: Vec2i, terrain: Grid[RegisteredType])
  extends DungeonLevel[ClientEntity](number, name, spawn, exit, terrain) {

  def addEntity(e: ClientEntity, at: Vec2i): Unit =
    entityIds(e.id.id) = e
    entityGrid(at) = e
    e.level = this
    e.position = at

  def deleteEntity(e: ClientEntity): Unit =
    entityIds.remove(e.id.id)
    entityGrid.remove(e.position)
    e.position = null
    e.level = null
}
