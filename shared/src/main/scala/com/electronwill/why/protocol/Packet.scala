package com.electronwill
package why.protocol

import niol.{NiolInput, NiolOutput}

abstract class Packet(val id: Int) {
  def writeData(out: NiolOutput): Unit
}

abstract class PacketParser[+P <: Packet](val id: Int) {
  def readData(in: NiolInput): P
}
