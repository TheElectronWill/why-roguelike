package com.electronwill.why.client.gametype

/** A client-side EntityType simply stores type data. */
final case class EntityType(val id: Int, val name: String, val defaultChar: Char)

object EntityType extends TypeRegistry[EntityType]
