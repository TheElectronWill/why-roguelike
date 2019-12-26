package com.electronwill.why.gametype

/** A type of a tile or entity.
  * @param name the type unique name
  * @param id the type id
  * @param character what to display for this type
  */
class RegisteredType(val name: String, val id: Int, val character: Char, val isBlocking: Boolean)
