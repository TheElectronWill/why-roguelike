package com.electronwill
package why
package server

import niol.network.tcp.{BufferSettings, ScalableSelector}
import niol.buffer.storage.StagedPools

class NetworkSystem(val port: Int) {
  private var selector: ScalableSelector = _
  private val bufferPool = StagedPools().directStage(500, 10, isMoreAllocationAllowed=false).build()
  // Allocate at most 10 buffers of at most 500 bytes of direct memory (off-heap)
  // If an incoming packet has a size > 500 bytes, throw an error.
  // Outgoing packets are not limited: any buffer can be sent to a client with ClientAttach.write(buff)

  /** Starts the TCP server. */
  def start(): Unit =
    // Creates the ScalableSelector
    // It will listen to incoming packets and process outgoing ones.
    val onStart = () => println("Server started")
    val onStop = () => println("Server stopped")
    val onError = (e: Exception) => {
      Logger.error("Error in server's NetworkSystem", e)
      false // false to stop the server on error, true to continue
    }
    selector = ScalableSelector(onStart, onStop, onError)

    // Defines how the memory is managed for incoming packets
    val bufferSettings = BufferSettings(baseBufferSize=500, bufferProvider=bufferPool)

    // Registers a new TcpListener to be notified when a new client connects
    // and when a client disconnects.
    val listener = WhyTcpListener()
    selector.listen(port, bufferSettings, listener)

    // Starts the server
    selector.start("server-thread")
    Thread.sleep(10000)
    selector.stop()

  /** Disconnects all the clients and stops the TCP server. */
  def stop(): Unit = selector.stop()
}
