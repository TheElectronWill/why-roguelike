package com.electronwill.collection

import scala.collection.mutable.Iterable
import scala.reflect.ClassTag

/**
 * A resizeable unordered collection whose remove(i) method moves the last
 * element to index i and is therefore O(1).
 *
 * @author TheElectronWill, for the Tuubes project
 * @see https://github.com/tuubes/TuubesCore
 * @see in TuubesCore: Bag.scala, SimpleBag.scala
 */
final class Bag[A >: Null <: AnyRef: ClassTag](initialCapacity: Int = 16) extends Iterable[A] {
  private[this] var array = new Array[A](initialCapacity)
  private[this] var s: Int = 0

  def size: Int = s
  def apply(i: Int): A = array(i)
  def remove(i: Int): Unit = {
    val last = array(s)
    array(i) = last
    array(s) = null
    s -= 1
  }
  def -=(elem: A): this.type = {
    var i = 0
    while (i < s && array(i) != elem) {
      i += 1
    }
    if (i != s) {
      remove(i)
    }
    this
  }
  def +=(elem: A): this.type = {
    if (array.length == s) {
      array = grow(array, s + (s >> 1))
    }
    array(s) = elem
    s += 1
    this
  }
  def ++=(arr: Array[A], offset: Int, length: Int): this.type = {
    val newS = s + length
    if (newS >= array.length) {
      array = grow(array, math.max(newS, s + (s >> 1)))
    }
    System.arraycopy(arr, offset, array, s, length)
    this
  }

  def ++=(bag: Bag[A], offset: Int = 0): this.type = {
    bag.addTo(this, offset)
    this
  }

  private def addTo(to: Bag[A], offset: Int): Unit = {
    to ++= (array, offset, s - offset)
  }

  def indexOf(elem: A): Int = {
    var i = 0
    while (i < s && array(i) != elem) {
      i += 1
    }
    if (i == s) -1 else i
  }

  def iterator: Iterator[A] = new Iterator[A] {
    private[this] var i = 0
    private[this] val l = s

    def hasNext: Boolean = i < l

    def next(): A = {
      val v = array(i)
      i += 1
      v
    }
  }

  def foreach[U](f: A => U): Unit = {
    val l = s
    var i = 0
    while (i < l) {
      f(array(i))
      i += 1
    }
  }

  def clear(): Unit = {
    var i = 0
    while (i < s) {
      array(i) = null
      i += 1
    }
    s = 0
  }
}

object Bag {
  private def grow[T: ClassTag](array: Array[T], newLength: Int): Array[T] = {
    val newArray = new Array[T](newLength)
    System.arraycopy(array, 0, newArray, 0, array.length)
    newArray
  }
}
