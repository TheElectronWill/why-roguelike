package com.electronwill.why.client

enum StandardColor {
  case Black, Red, Green, Yellow, Blue, Magenta, Cyan, White
}
enum ColorModifier(val codeOffset: Int) {
  case Bright extends ColorModifier(60)
  case Dark extends ColorModifier(0)
}
enum Mode(val codeOffset: Int) {
  case Foreground extends Mode(30)
  case Background extends Mode(40)
}
/**
 * ADT for possible ANSI colors.
 * @param ansi the ANSI SGR sequence that enables the color
 * @see http://dotty.epfl.ch/docs/reference/enums/adts.html for the syntax
 */
enum Color(val ansi: String) {
  case Standard(c: StandardColor, cm: ColorModifier, mode: Mode) extends Color(
    AnsiSequences.sgr(mode.codeOffset + cm.codeOffset + c.ordinal)
  )
  case Extended(code256: Int, mode: Mode) extends Color(
    AnsiSequences.sgr(mode.codeOffset + 8, 5, code256)
  )
  case True(r: Int, g: Int, b: Int, mode: Mode) extends Color(
    AnsiSequences.sgr(mode.codeOffset + 8, 2, r, g, b)
  )
}
