package com.electronwill.why
package ansi

enum StandardColor extends java.lang.Enum[StandardColor] {
  case Black, Red, Green, Yellow, Blue, Magenta, Cyan, White
}
enum StandardBrightness(val codeOffset: Int) {
  case Bright extends StandardBrightness(60)
  case Dark extends StandardBrightness(0)
}
/**
 * ADT for possible ANSI colors.
 * @see http://dotty.epfl.ch/docs/reference/enums/adts.html for the syntax
 */
enum Color {
  case Standard(c: StandardColor, b: StandardBrightness)
  case Extended(code256: Int)
  case True(r: Int, g: Int, b: Int)
}
