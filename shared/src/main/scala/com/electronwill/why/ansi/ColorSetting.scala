package com.electronwill.why
package ansi

final case class ColorSetting(fg: Option[Color], bg: Option[Color]) {
  def isEmpty: Boolean = fg.isEmpty && bg.isEmpty
  def isDefined: Boolean = fg.isDefined || bg.isDefined
}
