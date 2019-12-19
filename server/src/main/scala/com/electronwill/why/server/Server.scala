package com.electronwill
package why.server

import niol.network.tcp._ // com.electronwill.niol
import niol.buffer.storage._

import why.DungeonLevel
import scala.collection.mutable

object Server {
  inline val VERSION = "1.0.0-beta1"

  private val levels = mutable.LongMap[DungeonLevel]()
  //private val generator = gen.WalkingGenerator()
  
  private var port = -1
  private var selector: ScalableSelector = _
  private val bufferPool = StagedPools().directStage(500, 10, isMoreAllocationAllowed=false).build()
  // Allocate at most 10 buffers of at most 500 bytes of direct memory (off-heap)
  // If an incoming packet has a size > 500 bytes, throw an error.
  // Outgoing packets are not limited: any buffer can be sent to a client with ClientAttach.write(buff)

  /** Starts the TCP server on the given port. */
  def start(port: Int): Unit =
    // Creates the ScalableSelector
    // It will listen to incoming packets and process outgoing ones. 
    val onStart = () => println("Server started")
    val onStop = () => println("Server stopped")
    val onError = (e: Exception) => {
      println(s"[ERROR] $e")
      e.printStackTrace()
      false // false to stop the server on error, true to continue
    }
    selector = ScalableSelector(onStart, onStop, onError)

    // Defines how the memory is managed for incoming packets
    val bufferSettings = BufferSettings(baseBufferSize=500, bufferProvider=bufferPool)

    // Registers a new TcpListener to be notified when a new client connects
    // and when a client disconnects.
    val listener = protocol.WhyTcpListener()
    selector.listen(port, bufferSettings, listener)
    this.port = port

    // Starts the server
    selector.start("server-thread")
    Thread.sleep(1000)
    selector.stop()
  
  /** Disconnects all the clients and stops the TCP server. */
  def stop(): Unit = selector.stop()

  // --- --- ---
  def level(n: Int): DungeonLevel =
    val generator: gen.WalkingGenerator = null
    levels.getOrElseUpdate(n.toLong, generator.generate(n)) // TODO
}
