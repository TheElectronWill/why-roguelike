package com.electronwill.why.util

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
  private var array = new Array[A](initialCapacity)
  private var _size = 0

  private def grow(newLength: Int): Unit = {
    val newArray = new Array[T](newLength)
    System.arraycopy(array, 0, newArray, 0, array.length)
    array = newArray
  }

  def size: Int = _size
  def isEmpty: Boolean = _size == 0
  def nonEmpty: Boolean = _size > 0

  def apply(i: Int): A = array(i)

  def remove(i: Int): Unit = {
    val last = array(_size)
    array(i) = last
    array(_size) = null
    _size -= 1
  }

  def -=(elem: A): this.type = {
    var i = 0
    while (i < _size && array(i) != elem) {
      i += 1
    }
    if (i != _size) {
      remove(i)
    }
    this
  }

  def +=(elem: A): this.type = {
    if (array.length == _size) {
      grow(_size + (_size >> 1))
    }
    array(_size) = elem
    _size += 1
    this
  }

  def addAll(arr: Array[A], offset: Int, length: Int): this.type = {
    val newS = _size + length
    if (newS >= array.length) {
      grow(math.max(newS, _size + (_size >> 1)))
    }
    System.arraycopy(arr, offset, array, _size, length)
    this
  }

  def addAll(bag: Bag[A], offset: Int): this.type = {
    to ++= (array, offset, _size-offset)
    this
  }

  def ++=(bag: Bag[A]): this.type =
    this.addAll(bag, 0)

  def indexOf(elem: A): Int = {
    var i = 0
    while (i < _size && array(i) != elem) {
      i += 1
    }
    if (i == _size) -1 else i
  }

  def iterator: Iterator[A] = new Iterator[A] {
    private var i = 0
    private val l = _size

    def hasNext: Boolean = i < l

    def next(): A = {
      val v = array(i)
      i += 1
      v
    }
  }

  override def foreach[U](f: A => U): Unit = {
    var i = 0
    while (i < _size) {
      f(array(i))
      i += 1
    }
  }

  def clear(): Unit = {
    var i = 0
    while (i < _size) {
      array(i) = null
      i += 1
    }
    _size = 0
  }
}
