package com.electronwill.why.client

@main def whyClient(serverIP: String, serverPort: Int): Unit =
  Client.connect(serverIP, serverPort)
