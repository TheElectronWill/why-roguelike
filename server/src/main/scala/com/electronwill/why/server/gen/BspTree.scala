package com.elecronwill.why.server.gen

class BspTree(region: Box) {
  val root = BspNode(region)
}

class BspNode(val region: Box) {
  private var a: Node = null
  private var b: Node = null

  def childA = a
  def childB = b

  def isLeaf = a == null && a == null

  def split(horizontally: Boolean = region.isTall()): Unit = {
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
    a = Node(regionA)
    b = Node(regionB)
  }
}
