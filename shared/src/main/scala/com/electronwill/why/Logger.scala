package com.electronwill.why

import ansi._

object Logger {
  def ok(msg: String) = log(AnsiSequences.colorFg(StandardColor.Green), " OK ", msg)

  def info(msg: String) = log(AnsiSequences.resetSgr, "INFO", msg)

  def warn(msg: String) = log(AnsiSequences.colorFg(StandardColor.Yellow), "WARN", msg)
  def error(msg: String) = log(AnsiSequences.colorFg(StandardColor.Red), "ERROR", msg)

  def error(msg: String, err: Throwable): Unit =
    error(msg)
    err.printStackTrace()

  private def log(ansi: String, severity: String, msg: String) =
    System.err.print(ansi)
    System.err.print("[")
    System.err.print(severity)
    System.err.print("] ")
    System.err.println(msg)
}
