package com.electronwill.why

import ansi._

object Logger {
  def info(msg: String) = log(AnsiSequences.resetSgr, "INFO", msg)

  def warn(msg: String) = log(AnsiSequences.colorFg(StandardColor.Yellow), "WARN", msg)
  def error(msg: String) = log(AnsiSequences.colorFg(StandardColor.Red), "ERROR", msg)

  def error(msg: String, err: Throwable): Unit =
    error(msg)
    err.printStackTrace()

  private def log(ansi: String, severity: String, msg: String) =
    print(ansi)
    print("[")
    print(severity)
    print("] ")
    println(msg)
}
