package com.electronwill.why
package client.console

import ansi._
import java.io._

/** Console output.
  *
  * ==Coordinates==
  * In the terminal, the first (upper-left) character is (y=1,x=1).
  * Y is the line number, starting at one, increasing downwards.
  * X is the column number, starting at one, increasing towards right.
  */
object ConsoleOutput {

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

  /** Gets the current line. */
  def cursorLine = _line

  /** Gets the current column. */
  def cursorColumn = _col

  /** Returns `(cursorLine, cursorColumn)` */
  def cursorPosition = Vec2i(_line, _col)

  /** Moves the cursor to the specified position. */
  def moveCursor(line: Int, col: Int): Unit =
    assert(col > 0 && line > 0, s"Invalid position ($col, $line): x and y should be > 0")
    // Optimization here: we avoid to use the general moveTo to save some characters
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

  /** Writes a colored char at the given position. */
  def writeCharAt(line: Int, col: Int, ch: Char, color: ColorSetting = ColorSetting(None, None)): Unit =
    writeStrAt(col, line, ch.toString, color)

  /** Writes a colored message of **one** line at the given position. */
  def writeStrAt(line: Int, col: Int, str: String, color: ColorSetting = ColorSetting(None, None)): Unit =
    moveCursor(col, line)
    writeStr(str, color)

  /** Writes a colored char at the current position. */
  def writeChar(ch: Char, color: ColorSetting = ColorSetting(None, None)): Unit = writeStr(ch.toString, color)

  /** Writes a colored message of **one** line at the current position. */
  def writeStr(str: String, color: ColorSetting = ColorSetting(None, None)) =
    for fg <- color.fg do
      print(AnsiSequences.colorFg(fg))
    for bg <- color.bg do
      print(AnsiSequences.colorBg(bg))

    print(str)

    if color.isDefined
      print(AnsiSequences.resetSgr)

    _col += str.length

}
