package com.electronwill
package why.protocol

import niol.{NiolInput, NiolOutput}

abstract class Packet(val id: Int) {
  def writeData(out: NiolOutput): Unit
}

abstract class PacketParser[P <: Packet](val id: Int) {
  def readData(in: NiolInput): P
}

abstract class PacketRegistry {
  private val maxId = 255
  private val parsers = new Array[PacketParser[_]](maxId+1)

  def register(p: PacketParser[_]): Unit =
    parsers(p.id) = p

  def get(id: Int) = parsers(id)

  def parse(id: Int, in: NiolInput) = parsers(id).readData(in)

  def init(): Unit
}
