package com.electronwill
package why.server
package network

import why.Logger
import why.protocol.server.ServerPacket
import why.protocol.client.{ClientPacket, ConnectionRequest, PlayerMove}
import niol.buffer.{CircularBuffer, ExpandingOutput, NiolBuffer}
import niol.buffer.storage.{BytesStorage, StagedPools}
import niol.network.tcp.{ServerChannelInfos => SCI, _}

import java.nio.channels.{SelectionKey, SocketChannel}
import java.util.concurrent.atomic.AtomicInteger
import scala.util.{Try, Success, Failure}

class WhyClientAttach(sci: SCI[WhyClientAttach], channel: SocketChannel, key: SelectionKey)
  extends HAttach[WhyClientAttach](sci, channel, key) {

  val clientId: Int = WhyClientAttach.lastId.getAndIncrement() // thread-safe increment

  /** Constructs the packet header for the given data.
    * In WHY's protocol, the header is always 2 bytes for the packet length.
    * The packet's id is part of the data, because for the Niol library the header's
    * only purpose is to determine the data's length.
    */
  override protected def makeHeader(data: NiolBuffer): NiolBuffer =
    val buff = CircularBuffer(BytesStorage.allocateHeap(2))
    buff.writeShort(data.readableBytes & 0xffff) // packetLength
    Logger.info(s"Writing packet header: length = ${data.readableBytes}")
    buff

  /** Extracts the packet length from the header.
    * @return the packet length, or -1 if not enough data is available to tell
    */
  override protected def readHeader(buffer: NiolBuffer): Int =
    if buffer.readableBytes < 2 then
      -1
    else
      val len = buffer.readUnsignedShort()
      Logger.info(s"Received packet of length $len")
      len

  /** Reacts to a packet sent by the client. The buffer is guaranteed (by Niol)
    * to contain only the "data" part of only one packet.
    */
  override protected def handleData(incomingData: NiolBuffer): Unit =
    val packet: Try[ClientPacket] = ClientPacket.parse(incomingData)
    packet match
      case Failure(e) =>
        Logger.error(s"Error while handling incoming data from client $clientId", e)
        disconnect()

      case Success(p) =>
        Logger.info(s"Received packet from client $clientId: $p")
        PacketHandler.handlePacket(p, this)

  def sendPacket(packet: ServerPacket, completionHandler: () => Unit = ()=>()) =
    val output = ExpandingOutput(1024, WhyClientAttach.outputBufferPool)
    ServerPacket.write(packet, output)
    Logger.info(s"Sending packet $packet")
    this.write(output.asBuffer, completionHandler)

  def disconnect() =
    key.cancel()
    channel.close()
    Logger.ok(s"Connection closed for client $clientId.")
}
object WhyClientAttach {
  private val lastId = AtomicInteger(0)
  private val outputBufferPool = StagedPools().directStage(100, 10, true).defaultAllocateHeap().build()
}
