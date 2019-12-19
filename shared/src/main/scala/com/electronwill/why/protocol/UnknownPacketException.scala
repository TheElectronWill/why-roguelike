package com.electronwill.why.protocol

case class UnknownPacketException(packetId: Int, packetInfo: String)
  extends Exception(s"No $packetInfo packet has id $packetId")
