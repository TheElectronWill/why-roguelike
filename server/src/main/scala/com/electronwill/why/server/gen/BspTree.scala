package com.electronwill.why.server.gen

import com.electronwill.why.util.Box

class BspTree(region: Box) {
  val root = BspNode(region)
}

class BspNode(val box: Box) {
  private var a: BspNode = null
  private var b: BspNode = null

  def childA = a
  def childB = b

  def isLeaf = a == null && a == null

  def split(horizontally: Boolean = box.isTall): Unit = {
    val boxCenter = box.roundedCenter
    var (regionA, regionB) =
      if (horizontally) {
        (
          Box.intervals(box.xInterval, (boxCenter.y + 1, box.yMax)),
          Box.intervals(box.xInterval, (box.yMin, boxCenter.y))
        )
      } else {
        (
          Box.intervals((boxCenter.x + 1, box.xMax), box.yInterval),
          Box.intervals((box.xMin, boxCenter.x), box.yInterval)
        )
      }
    a = BspNode(regionA)
    b = BspNode(regionB)
  }
}
