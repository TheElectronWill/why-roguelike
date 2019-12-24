package com.electronwill.why
package client

import java.io._
import ansi._

/** Terminal UI */
object TUI {
  /** Runs the `tput` command and returns its output. */
  private def tput(cmd: String): String =
    val p = Runtime.getRuntime.exec(Array("tput", cmd))
    p.waitFor()
    val reader = BufferedReader(InputStreamReader(p.getInputStream))
    val line = reader.readLine()
    reader.close()
    line

  private var _x = 0
  private var _y = 0

  /** Gets height (in lines) of the terminal. */
  def height: Int = tput("lines").toInt

  /** Gets the width (in columns) of the terminal. */
  def width: Int = tput("cols").toInt

  def cursorX = _x
  def cursorY = _y
  def cursor = Vec2i(_x, _y)

  def moveCursor(x: Int, y: Int): Unit =
    // Avoid to use the general moveTo to save some characters
    if x == _x
      if y > _y
        print(AnsiSequences.move(Direction.DOWN, y-_y))
      if y < _y
        print(AnsiSequences.move(Direction.UP, _y-y))
    else if y == _y
      if x > _x
        print(AnsiSequences.move(Direction.RIGHT, x-_x))
      else if x < _x
        print(AnsiSequences.move(Direction.LEFT, _x-x))
    else
      print(AnsiSequences.moveTo(y, x))
    _x = x
    _y = y

  def write(x: Int, y: Int, ch: Char): Unit =
    write(x, y, ch, ColorSetting(None, None))

  def write(x: Int, y: Int, ch: Char, color: ColorSetting): Unit =
    write(x, y, ch.toString, color)

  /** Writes a colored message of **one** line at the given position. */
  def write(x: Int, y: Int, str: String, color: ColorSetting) =
    moveCursor(x, y)

    for fg <- color.fg do
      print(AnsiSequences.colorFg(fg))
    for bg <- color.bg do
      print(AnsiSequences.colorBg(bg))

    print(str)

    if color.fg.nonEmpty || color.bg.nonEmpty
      print(AnsiSequences.resetSgr)

    _x += str.length

}
