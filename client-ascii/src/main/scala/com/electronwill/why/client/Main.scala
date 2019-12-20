package com.electronwill.why.client

@main def why(serverIP: String, serverPort: Int): Unit = {
  println(s"Connecting to $serverIP on port $serverPort")
}
