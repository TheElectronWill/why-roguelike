package com.electronwill.why.server

@main def server(): Unit =
  Server.network.start()
