package com.electronwill.why
package client
package console

import scala.io.StdIn
import sys.process._
import java.nio.charset.StandardCharsets.UTF_8
import ConsoleInput.Input._

/** Reacts to player's input. */
class InputHandler extends Runnable {
  @volatile var running = true

  def run(): Unit =
    ConsoleInput.init()
    while running do
      ConsoleInput.read() match
        case EOS =>
          running = false
        case Arrow(dir) =>
          Client.move(dir)
        case Text(str) =>
          Logger.info(s"Received text $str")
        case a =>
          Logger.info(s"Received $a")

  private def read(): Int = java.lang.System.in.read() // Console.in.read()
}
