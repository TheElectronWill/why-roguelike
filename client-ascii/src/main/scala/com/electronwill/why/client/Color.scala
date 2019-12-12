package com.electronwill.why.client

enum Color8 {
  case Black, Red, Green, Yellow, Blue, Magenta, Cyan, White
}
enum Modifier8(val ainsiOffset: Int) {
  case Bright(0)
  case Dark(10)
}
enum Mode(val ainsiOffset: Int) {
  case Foreground(30)
  case Background(40)
}
enum Color(val ainsi: String) {
  case Standard(c: Color8, m: Modifier8, at: Mode) extends Color(
    (at.ainsiOffset + m.ainsiOffset + c.ordinal).toString
  )
  case Custom(r: Int, g: Int, b: Int, at: Mode) extends Color(
    s"${at.ainsiOffset+8};2;$r;$g;$b"
  )
}
