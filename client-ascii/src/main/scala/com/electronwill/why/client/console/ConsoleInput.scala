package com.electronwill.why
package client.console

import geom._

import sys.process._
import java.nio.charset.StandardCharsets.UTF_8

object ConsoleInput {
  /** Configures the console. Only works on UNIX-like systems, i.e. not on Windows. */
  def init(): Unit =
    // /dev/tty is the console of the current process.
    assert(!System.getProperty("os.name").toLowerCase.contains("windows"), "Unsupported OS")
    (Seq("sh", "-c", "stty -icanon min 1 < /dev/tty") !) // pass all input immediately (buffer only 1 byte)
    (Seq("sh", "-c", "stty -echo < /dev/tty") !) // disable tty echo
    (Seq("sh", "-c", "stty iutf8 < /dev/tty") !) // treat input as UTF-8

  private def readByte(): Int = java.lang.System.in.read()

  def read(): Input =
    val first = readByte()
    if first == -1
      Input.EOS
    else if first >> 7 == 1 // At least two UTF-8 bytes
      val second = readByte()
      val text = String(Array(first.toByte, second.toByte), UTF_8)
      Input.Text(text)
    else if first == 27 // ESC
      val next = readByte()
      if next == 91 // ']'
        handleCSI(readByte())
      else
        Input.Escape
    else if first == 9
      Input.Tab
    else if first == 8
      Input.Backspace
    else
      Input.Text(first.toChar.toString)

  private def handleCSI(code: Int): Input =
    code match
      case 65 => Input.Arrow(Direction.UP)
      case 66 => Input.Arrow(Direction.DOWN)
      case 67 => Input.Arrow(Direction.RIGHT)
      case 68 => Input.Arrow(Direction.LEFT)
      case _  => Input.Sequence(code)

  enum Input {
    case Arrow(val direction: Direction)
    case Text(val text: String)
    case Backspace
    case Escape
    case Tab
    case Sequence(val code: Int)
    case EOS // End of stream
  }
}
