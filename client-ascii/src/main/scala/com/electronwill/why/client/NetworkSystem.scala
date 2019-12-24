package com.electronwill
package why
package client

import protocol.client._
import protocol.server._

import niol.{NiolInput, NiolOutput}
import niol.buffer.ExpandingOutput
import niol.buffer.storage.StagedPools
import niol.compatibility.{JavaDataInput, JavaDataOutput}
import java.net.Socket
import java.io.{DataInputStream, DataOutputStream}

class NetworkSystem(serverAddress: String, serverPort: Int) {
  private val bufferPool = StagedPools().directStage(100, 2, true).defaultAllocateHeap().build()
  private val socket = Socket(serverAddress, serverPort)

  private val javaOutput = DataOutputStream(socket.getOutputStream)
  private val output = JavaDataOutput(javaOutput)
  private val input = JavaDataInput(DataInputStream(socket.getInputStream))
  private var reader = PacketReader(input)

  def start() =
    Logger.info("Starting the network system")
    send(ConnectionRequest(Client.VERSION, Client.username))
    Thread(reader).start()

  def stop() =
    reader.running = false

  def send(packet: ClientPacket): Unit =
    val packetOutput = ExpandingOutput(32, bufferPool)
    ClientPacket.write(packet, packetOutput)
    val packetBuffer = packetOutput.asBuffer
    output.writeShort(packetBuffer.readableBytes)
    output.write(packetBuffer)
    javaOutput.flush()
}
