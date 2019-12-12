package com.electronwill.why
package client

class Terminal {


}
/**
 * Terminal control sequences.
 * @see https://en.wikipedia.org/wiki/ANSI_escape_code
 */
object Sequences {
  inline val BEL = 0x07.toChar // beeps
  inline val BS  = 0x08.toChar // backspaces one column
  inline val HT  = 0x09.toChar // goes to next tab stop
  inline val LF  = 0x0A.toChar // \n
  inline val CR  = 0x0D.toChar // \r
  inline val CAN = 0x18.toChar // Ctrl-X
  inline val SUB = 0x1A.toChar // Ctrl-Z
  inline val ESC = 0x1B.toChar // ESC
  inline val CSI = 0x9B.toChar // mostly equivalent to ESC [

  inline def csi(param: String | Int, end: Char) = s"$CSI$param$end"
  inline def csi(end: String, params: Int*) =
    params.mkString(CSI.toString, ';', end)

  def move(dir: Direction, n: Int) =
    val dirChar = dir match
      case UP    => 'A'
      case DOWN  => 'B'
      case RIGHT => 'C'
      case LEFT  => 'D'
    csi(n, dirChar)

  def moveLineUp(n: Int) = csi(n, 'E')
  def moveLineDown(n: Int) = csi(n, 'F')

  def moveToColumn(i: Int) = csi(n, 'G')
  def moveTo(line: Int, col: Int) = csi('G', line, col)

  def scrollUp(n: Int) = csi(n, 'S')
  def scrollDown(n: Int) = csi(n, 'T')

  def clearScreenFromCursor = csi(0, 'J')
  def clearScreenToCursor = csi(1, 'J')
  def clearScreen = csi(2, 'J')
  def clearScreenAndScrollback = csi(3, 'J')

  def clearLineFromCursor = csi(0, 'K')
  def clearLineToCursor = csi(1, 'K')
  def clearLine = csi(2, 'K')
  def clearLineAndScrollback = csi(3, 'K')

  def showCursor = csi("?25", 'h')
  def hideCursor = csi("?25", 'l')

  def markPosition = csi("", 's')
  def resetPosition = csi("", 'u')
}
