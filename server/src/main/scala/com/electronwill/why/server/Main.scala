package com.electronwill.why.server

@main def whyServer(): Unit =
  Server.registerTypes()
  Server.network.start()
