package com.electronwill
package why
package client

import protocol.server._
import niol._
import scala.util.{Try, Failure, Success}

class PacketReader(private val in: NiolInput) extends Runnable {
  @volatile var running = true

  def run(): Unit =
    Logger.info("Client networking thread started.")
    while running do
      val packetLength = in.readUnsignedShort()
      val packet: Try[ServerPacket] = ServerPacket.parse(in)
      packet match
        case Failure(e) =>
          Logger.error(s"Error while handling incoming data from the server.", e)
          e.printStackTrace()

        case Success(p) =>
          Logger.info(s"Received a packet from the server: $p")
          PacketHandler.handle(p)
}
