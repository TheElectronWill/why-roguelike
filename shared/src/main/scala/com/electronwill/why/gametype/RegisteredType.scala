package com.electronwill.why.gametype

/** A type of a tile or entity.
  * @param name the type unique name
  * @param id the type id
  * @param character what to display for this type
  */
final case class RegisteredType(name: String,  id: Int,  character: Char,  isBlocking: Boolean)
