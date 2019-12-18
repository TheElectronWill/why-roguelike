package com.electronwill.why
package client.gametype

import gametype.TileTypeData

/** A client-side tile registry whose content is controlled by the server (packet S->C #1 IdRegistration). */
object TileType extends TypeRegistry[TileTypeData]
