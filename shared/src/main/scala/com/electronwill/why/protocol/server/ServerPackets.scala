package com.electronwill.why.protocol
package server

/**
 * Packets sent by the server to the client.
 */
object ServerPackets extends PacketRegistry {
  override def init() =  
    register(AreaUpdate)
    register(ConnectionResponse)
    register(EntityAppearance)
    register(EntityDelete)
    register(EntityMove)
    register(EntitySpawn)
    register(IdRegistration)
    register(TerrainData)
    register(TileAppearance)
    register(TileUpdate)
    register(Warning)

    register(Disconnect)
}
