package com.electronwill
package why.protocol

import niol.{NiolInput, NiolOutput}
import why.util.Vec2i

/** Extend Niol capabilities to support WHY structures */

def (in: NiolInput) readVector(): Vec2i =
  Vec2i(in.readUnsignedShort, in.readUnsignedShort)

def (out: NiolOutput) writeVector(v: Vec2i): Unit =
  out.writeShort(v.x)
  out.writeShort(v.y)
