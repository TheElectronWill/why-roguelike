package com.electronwill.why.server

@main def server(port: Int): Unit =
  println(s"Starting WHY server on port $port...")
  Server.start(port)
