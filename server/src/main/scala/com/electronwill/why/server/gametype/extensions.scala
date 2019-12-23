package com.electronwill.why
package server.gametype

import gametype.{Tile, Entity}

/** @return the TileType associated with this Tile. */
def [A <: Tile](a: A) tpe: TileType[A] = TileType.typeOf(a.getClass).asInstanceOf[TileType[A]]

/** @return the EntityType associated with this Entity. */
def [A <: Entity](a: A) tpe: EntityType[A] = EntityType.typeOf(a.getClass).asInstanceOf[EntityType[A]]
