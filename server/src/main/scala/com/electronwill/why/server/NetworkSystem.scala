package com.electronwill
package why
package server

import niol.network.tcp.{BufferSettings, ScalableSelector}
import niol.buffer.storage.StagedPools

class NetworkSystem(val port: Int) {
  private var selector: ScalableSelector = _
  private val bufferPool = StagedPools().directStage(496, 10, isMoreAllocationAllowed=false).defaultAllocateHeap().build()
  // Allocate at most 10 buffers of at most 496 bytes of direct memory (off-heap)
  // And an unlimited number of heap memory.
  // Outgoing packets are not limited: any buffer can be sent to a client with ClientAttach.write(buff)

  /** Starts the TCP server. */
  def start(): Unit =
    // Creates the ScalableSelector
    // It will listen to incoming packets and process outgoing ones.
    val onStart = () => Logger.ok("Server started")
    val onStop = () => Logger.info("Server stopped")
    val onError = (e: Exception) => {
      Logger.error("Error in server's NetworkSystem", e)
      false // false to stop the server on error, true to continue
    }
    selector = ScalableSelector(onStart, onStop, onError)

    // Defines how the memory is managed for incoming packets
    val bufferSettings = BufferSettings(baseBufferSize=100, bufferProvider=bufferPool)

    // Registers a new TcpListener to be notified when a new client connects
    // and when a client disconnects.
    val listener = WhyTcpListener()
    selector.listen(port, bufferSettings, listener)

    // Starts the server
    selector.start("server-thread")
    Runtime.getRuntime.addShutdownHook(Thread(() => selector.stop()))

  /** Disconnects all the clients and stops the TCP server. */
  def stop(): Unit = selector.stop()
}
