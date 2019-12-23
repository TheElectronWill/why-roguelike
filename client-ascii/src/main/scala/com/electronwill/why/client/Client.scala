package com.electronwill.why
package client

object Client {
  inline val VERSION = "1.0.0-beta1"

  var level: DungeonLevel = _
  var playerId: Int = -1
  var playerPosition = Vec2i.Zero

  private var networkSystem: NetworkSystem = _

  def network = networkSystem // getter

  def connect(serverAddress: String, serverPort: Int) =
    networkSystem = NetworkSystem(serverAddress, serverPort)
    networkSystem.start()
}
