package com.electronwill
package why.client

import collection.RecyclingIndex
import gametype._
import why.{DungeonLevel, Direction, Grid, Vec2i}
import why.gametype.{EntityId, RegisteredType}
import Visibility._

class ClientDungeonLevel(number: Int, name: String, spawn: Vec2i, exit: Vec2i, terrain: Grid[RegisteredType])
    extends DungeonLevel[ClientEntity](number, name, spawn, exit, terrain) {

  /** Stores which tiles are visible by the player. */
  private val visionGrid = Grid[Visibility](width, height, Hidden)

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

  def getVisibility(p: Vec2i): Visibility = visionGrid(p)
  def getVisibility(x: Int, y: Int): Visibility = visionGrid(x, y)

  def getVisibleTile(p: Vec2i): RegisteredType = getVisibleTile(p.x, p.y)
  def getVisibleTile(x: Int, y: Int): RegisteredType =
    if visionGrid(x, y) == Hidden then Tiles.get("void") else terrain(x, y)

  def updatePlayerVision(pos: Vec2i, dir: Direction): Unit =
    visionGrid.updateAll(v => if v == Visible then Seen else v)
    visionGrid(pos) = Visible

    val newVision = computeVision(pos, dir)
    for visible <- newVision do
      visionGrid(visible) = Visible

    for adjacent <- terrain.coordSquareAround(pos, 1) do
      visionGrid(adjacent) = Visible
}
