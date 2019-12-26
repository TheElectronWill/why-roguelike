package com.electronwill.why
package ansi

import Direction._
import Color._

/** Terminal control sequences.
  * @author TheElectronWill
  * @see https://en.wikipedia.org/wiki/ANSI_escape_code
  */
object AnsiSequences {
  inline val BEL: Char = 0x07 // beeps
  inline val BS: Char = 0x08 // backspaces one column
  inline val HT: Char = 0x09 // goes to next tab stop
  inline val LF: Char = 0x0A // \n
  inline val CR: Char = 0x0D // \r
  inline val CAN: Char = 0x18 // Ctrl-X
  inline val SUB: Char = 0x1A // Ctrl-Z
  inline val ESC: Char = 0x1B // ESC
  inline val CSI: Char = 0x9B // mostly equivalent to ESC [

  inline def csi(end: Char, param: String | Int) = s"$CSI$param$end"
  inline def csi(end: String, params: Int*) = params.mkString(CSI.toString, ";", end)

  // cursor control with CSI sequences
  def move(dir: Direction, n: Int) =
    val dirChar = dir match
      case UP    => 'A'
      case DOWN  => 'B'
      case RIGHT => 'C'
      case LEFT  => 'D'
    csi(dirChar, n)

  def moveLineUp(n: Int) = csi('E', n)
  def moveLineDown(n: Int) = csi('F', n)

  def moveToColumn(col: Int) = csi('G', col)
  def moveTo(line: Int, col: Int) = csi("H", line, col)

  def scrollUp(n: Int) = csi('S', n)
  def scrollDown(n: Int) = csi('T', n)

  def clearScreenFromCursor = csi('J', 0)
  def clearScreenToCursor = csi('J', 1)
  def clearScreen = csi('J', 2)
  def clearScreenAndScrollback = csi('J', 3)

  def clearLineFromCursor = csi('K', 0)
  def clearLineToCursor = csi('K', 1)
  def clearLine = csi('K', 2)
  def clearLineAndScrollback = csi('K', 3)

  def showCursor = csi("?25", 'h')
  def hideCursor = csi("?25", 'l')

  def markPosition = csi("", 's')
  def resetPosition = csi("", 'u')

  // SGR (Select Graphic Rendition) codes to set displays attributes
  inline def sgr(code: Int): String = csi('m', code)
  inline def sgr(params: Int*): String = csi("m", params: _*)

  def resetSgr = sgr(0)
  def bold = sgr(1)
  def faint = sgr(2)
  def underline = sgr(4)
  def blink = sgr(5)
  def swapColors = sgr(7) // swaps foreground and background colors
  def crossed = sgr(9)
  def normal = sgr(22)

  def colorFg(c: StandardColor): String =
    colorFg(Color.Standard(c, StandardBrightness.Dark))

  def colorFg(c: Color) = c match
    case Standard(sc, brightness) =>
      sgr(30 + brightness.codeOffset + sc.ordinal)
    case Extended(code) =>
      sgr(38, 5, code)
    case True(r,g,b) =>
      sgr(38, 2, r, g, b)

  def colorBg(c: StandardColor): String =
    colorBg(Color.Standard(c, StandardBrightness.Dark))

  def colorBg(c: Color) = c match
    case Standard(sc, brightness) =>
      sgr(40 + brightness.codeOffset + sc.ordinal)
    case Extended(code) =>
      sgr(48, 5, code)
    case True(r, g, b) =>
      sgr(48, 2, r, g, b)
}
