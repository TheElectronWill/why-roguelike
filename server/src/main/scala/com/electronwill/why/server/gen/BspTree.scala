package com.electronwill.why.server.gen

import com.electronwill.why.Box

class BspTree(region: Box) {
  val root = BspNode(region)

  def foreach[U](f: BspNode => U): Unit = walk(root, f)

  private def walk(node: BspNode, f: BspNode => Any): Unit =
    f(node)
    if node.childA != null then walk(node.childA, f)
    if node.childB != null then walk(node.childB, f)
}

class BspNode(var box: Box) {
  private var a: BspNode = null
  private var b: BspNode = null

  def childA = a
  def childB = b

  def isLeaf = a == null && b == null

  /** Creates two sub-boxes by splitting this box in two.
    * The results are saved in `childA` and `childB`.
    */
  def split(horizontally: Boolean = box.isTall): Unit =
    val (subA, subB) = box.split(horizontally)
    a = BspNode(subA)
    b = BspNode(subB)
}
