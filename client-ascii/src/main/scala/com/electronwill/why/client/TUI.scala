package com.electronwill.why
package client

import java.io._
import ansi._

/** Terminal UI
  *
  * ==Coordinates==
  * In the terminal, the first (upper-left) character is (y=1,x=1).
  * Y is the line number, starting at one, increasing downwards.
  * X is the column number, starting at one, increasing towards right.
  */
object TUI {
  /** Runs the `tput` command and returns its output. */
  private def tput(cmd: String): String =
    val p = Runtime.getRuntime.exec(Array("tput", cmd))
    p.waitFor()
    val reader = BufferedReader(InputStreamReader(p.getInputStream))
    val line = reader.readLine()
    reader.close()
    line

  private var _col = 0
  private var _line = 0

  /** Gets height (in lines) of the terminal. */
  def height: Int = tput("lines").toInt

  /** Gets the width (in columns) of the terminal. */
  def width: Int = tput("cols").toInt

  /** Clears the screen and places the cursor in the upper-left position (1,1) */
  def clear(): Unit =
    print(AnsiSequences.clearScreen)
    moveCursor(1, 1)

  def cursorLine = _line
  def cursorColumn = _col
  def cursorPosition = Vec2i(_line, _col)

  def moveCursor(line: Int, col: Int): Unit =
    assert(col > 0 && line > 0, s"Invalid position ($col, $line): x and y should be > 0")
    // Optimization here: avoid to use the general moveTo to save some characters
    if _col == 0 && _line == 0 // not initialized yet
      print(AnsiSequences.moveTo(line, col))
    else if col == _col
      if line > _line
        print(AnsiSequences.move(Direction.DOWN, line-_line))
      if line < _line
        print(AnsiSequences.move(Direction.UP, _line-line))
    else if line == _line
      if col > _col
        print(AnsiSequences.move(Direction.RIGHT, col-_col))
      else if col < _col
        print(AnsiSequences.move(Direction.LEFT, _col-col))
    else
      print(AnsiSequences.moveTo(line, col))
    // Update local state
    _line = line
    _col = col

  def write(line: Int, col: Int, ch: Char): Unit =
    write(col, line, ch, ColorSetting(None, None))

  def write(line: Int, col: Int, ch: Char, color: ColorSetting): Unit =
    write(col, line, ch.toString, color)

  /** Writes a colored message of **one** line at the given position. */
  def write(line: Int, col: Int, str: String, color: ColorSetting = ColorSetting(None, None)): Unit =
    moveCursor(col, line)
    write(str, color)

  def write(ch: Char): Unit = write(ch.toString)

  def write(ch: Char, color: ColorSetting): Unit = write(ch.toString, color)

  def write(str: String): Unit = write(str, ColorSetting(None, None))

  /** Writes a colored message of **one** line at the current position. */
  def write(str: String, color: ColorSetting) =
    for fg <- color.fg do
      print(AnsiSequences.colorFg(fg))
    for bg <- color.bg do
      print(AnsiSequences.colorBg(bg))

    print(str)

    if color.fg.nonEmpty || color.bg.nonEmpty
      print(AnsiSequences.resetSgr)

    _col += str.length

}
