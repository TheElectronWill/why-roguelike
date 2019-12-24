package com.electronwill.why.protocol
package server

/** Marker class for packets sent by the server to the client. */
abstract class ServerPacket(id: Int) extends Packet(id) // abstract for ease of use
object ServerPacket extends PacketRegistry[ServerPacket] {
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
