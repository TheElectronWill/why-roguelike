package com.electronwill.why.gametype

/** A type of "thing" that has been registered to a registry
 * @tparam B the type of "things" instances
 * @param build function that builds instances of "thing"
 * @param id the type id
 */
 abstract class RegisteredType[+B](val build: ()=>B, val id: Int)
