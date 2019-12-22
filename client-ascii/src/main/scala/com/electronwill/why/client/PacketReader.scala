package com.electronwill
package why
package client

import protocol.server._
import niol._
import scala.util.{Try, Failure, Success}

class PacketReader(private val in: NiolInput) extends Runnable {
  @volatile var running = true

  def run(): Unit =
    println("[INFO] Client networking thread started.")
    while running do
      val packetLength = in.readUnsignedShort()
      val packet: Try[ServerPacket] = ServerPacket.parse(in)
      packet match
        case Failure(e) =>
          println(s"[ERROR] Error while handling incoming data from the server.")
          println(s"[ERROR] $e")
          e.printStackTrace()

        case Success(p) =>
          println(s"[INFO] Received a packet from the server: $p")
          PacketHandler.handle(p)
}
