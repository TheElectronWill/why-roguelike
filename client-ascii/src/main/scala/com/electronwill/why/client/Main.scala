package com.electronwill.why.client

@main def why(serverIP: String, serverPort: Int): Unit =
  Client.connect(serverIP, serverPort)
