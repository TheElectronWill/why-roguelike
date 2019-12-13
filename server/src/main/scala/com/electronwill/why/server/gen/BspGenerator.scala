package com.electronwill
package why.server.gen

import collection.Bag
import why.util.Box
import why.DungeonLevel

/**
 * Un générateur de niveau basé sur un arbre binaire de partitionnement
 * de l'espace (Binary Space Partitioning Tree, abrégé "BSP Tree").
 *
 * L'idée est de diviser l'espace en deux de façon récursive et de créer
 * une salle dans chaque feuille de l'arbre. Les salles qui ont le même
 * noeud parent sont ensuite reliées par un chemin.
 */
class BspGenerator(private val width: Int,
                   private val height: Int,
                   private val minRoomDim: Int,
                   private val minSplits: Int,
                   private val maxSplits: Int)
  extends LevelGenerator {

  def generate(level: Int): DungeonLevel = {
    val tree = BspTree(Box.positive(width, height))
    val candidates = Bag[BspNode]()
    candidates += tree.root
    for (x <- 1 to maxSplits if candidates.nonEmpty) {
      // TODO select a random node and split it
      candidates += chosen.a
      candidates += chosen.b
    }
    null // TODO
  }
}
