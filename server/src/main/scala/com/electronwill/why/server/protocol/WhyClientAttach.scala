package com.electronwill
package why.server.protocol

import why.protocol.Packet
import why.protocol.client.{ClientPackets, ConnectionRequest, PlayerMove}
import niol.buffer.{NiolBuffer, CircularBuffer, storage}
import niol.network.tcp.{ServerChannelInfos => SCI, _}

import java.nio.channels.{SelectionKey, SocketChannel}
import java.util.concurrent.atomic.AtomicInteger
import scala.util.{Try, Success, Failure}

class WhyClientAttach(sci: SCI[WhyClientAttach], channel: SocketChannel, key: SelectionKey)
  extends HAttach[WhyClientAttach](sci, channel, key) {

  val clientId = WhyClientAttach.lastId.getAndIncrement() // thread-safe increment

  /** Constructs the packet header for the given data.
    * In WHY's protocol, the header is always 2 bytes for the packet length.
    * The packet's id is part of the data, because for the Niol library the header's
    * only purpose is to determine the data's length.
    */
  override protected def makeHeader(data: NiolBuffer): NiolBuffer =
    val buff = CircularBuffer(storage.BytesStorage.allocateHeap(2))
    buff.writeShort(data.readableBytes) // packetLength
    buff
  
  /** Extracts the packet length from the header.
    * @return the packet length, or -1 if not enough data is available to tell
    */
  override protected def readHeader(buffer: NiolBuffer): Int =
    if buffer.readableBytes < 2 then -1 else buffer.readUnsignedShort()

  /** Reacts to a packet sent by the client. The buffer is guaranteed (by Niol)
    * to contain only the "data" part of only one packet.
    */
  override protected def handleData(incomingData: NiolBuffer): Unit =
    val packet: Try[Packet] = ClientPackets.parse(incomingData)
    packet match
      case Failure(e) =>
        println(s"[ERROR] Error while handling incoming data from client $clientId")
        println(s"[ERROR] $e")
        e.printStackTrace()
      
      case Success(p) =>
        println(s"[INFO] Received packet from client $clientId: $p")

}
object WhyClientAttach {
  private val lastId = AtomicInteger(0)
}
