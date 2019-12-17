package com.electronwill.why.gametype

/** A type of "thing" that has been registered to a registry
  * @tparam B the type of "things" instances
  * @param make function that creates instances of "thing"
  * @param id the type id
  */
 abstract class RegisteredType[+B](val make: ()=>B, val id: Int)
