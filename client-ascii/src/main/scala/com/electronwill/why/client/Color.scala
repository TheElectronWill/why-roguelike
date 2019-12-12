package com.electronwill.why.client

enum StandardColor {
  case Black, Red, Green, Yellow, Blue, Magenta, Cyan, White
}
enum ColorModifier(val codeOffset: Int) {
  case Bright(60)
  case Dark(0)
}
enum Mode(val codeOffset: Int) {
  case Foreground(30)
  case Background(40)
}
/**
 * ADT for possible ANSI colors.
 * @param ansi the ANSI SGR sequence that enables the color
 * @see http://dotty.epfl.ch/docs/reference/enums/adts.html for the syntax
 */
enum Color(val ansi: String) {
  import AnsiSequences.sgr
  case Standard(c: StandardColor, cm: ColorModifier, mode: Mode) extends Color(
    sgr(mode.codeOffset + cm.codeOffset + c.ordinal)
  )
  case Extended(code256: Int, mode: Mode) extends Color(
    sgr(mode.codeOffset + 8, 5, code256)
  )
  case True(r: Int, g: Int, b: Int, mode: Mode) extends Color(
    sgr(mode.codeOffset + 8, 2, r, g, b)
  )
}
