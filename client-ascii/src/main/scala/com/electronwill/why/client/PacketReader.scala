package com.electronwill
package why
package client

import protocol.server._
import niol._
import niol.buffer.CircularBuffer
import niol.buffer.storage.BytesStorage
import scala.util.{Try, Failure, Success}

class PacketReader(private val in: NiolInput) extends Runnable {
  @volatile var running = true

  def run(): Unit =
    Logger.info("Client networking thread started.")
    while running do
      val packetLength = in.readUnsignedShort()
      if packetLength == 65535
        Logger.warn(s"Got packet of length 65535, but that is probably because the channel has been closed and we've read -1")
        Logger.warn(s"Stopping the client now.")
        System.exit(0)
      else
        Logger.info(s"Got packet of length $packetLength")
        val packetBuffer = CircularBuffer(BytesStorage.allocateHeap(packetLength))
        in.read(packetBuffer)
        Logger.info("Read the whold packet.")

        val packet: Try[ServerPacket] = ServerPacket.parse(packetBuffer)
        packet match
          case Failure(e) =>
            Logger.error(s"Error while handling incoming data from the server.", e)

          case Success(p) =>
            Logger.info(s"Received a packet from the server: $p")
            PacketHandler.handle(p)
}
