package com.electronwill.why
package client

import java.io.PrintStream
import java.io.File

@main def whyClient(serverIP: String, serverPort: Int): Unit =
  System.setErr(PrintStream(File("why-client.log")))
  val os = System.getProperty("os.name") + " " + System.getProperty("os.version")
  Logger.info(s"Starting WHY client in a ${TUI.width}x${TUI.height} terminal, on $os")
  Client.connect(serverIP, serverPort)
