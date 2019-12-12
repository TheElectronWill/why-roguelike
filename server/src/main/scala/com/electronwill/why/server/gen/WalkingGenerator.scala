package com.electronwill.why.server.gen

import scala.util.Random
import com.electronwill.why._
import com.electronwill.why.util._
import com.electronwill.why.util.Extensions._

/**
 * Un générateur de niveau basé sur de la marche aléatoire.
 * Le principe est simple : on commence avec une grille remplie et une position
 * aléatoire sur cette grille. On vide cette case puis on se déplace dans une
 * direction aléatoire, et on recommence.
 *
 * @param width nombre de colonnes de la grille
 * @param height nombre de lignes de la grille
 * @param fill_factor pourcentage de cases à vider, par défaut 0.75
 */
class WalkingGenerator(private val width: Int,
                       private val height: Int,
                       private val emptyFactor: Float,
                       minExitDistance: Float)
  extends LevelGenerator {

  private val squaredMinExitDistance = minExitDistance*minExitDistance

  def generate(level: Int): DungeonLevel = {
    val grid = Grid[Tile](width, height, Tiles.Wall)
    val target = (width*height*emptyFactor).toInt
    val spawn = Vec2i.random(Vec2i(0,0), Vec2i(width, height))
    var exit: Vec2i = null
    var pos = spawn
    var i = 0
    while (i < target) {
      // On vide la cellule courante
      if (grid(pos) == Tiles.Wall) {
        val cell = generateCell(pos, spawn, exit == null)
        if (cell == Tiles.StairsDown) {
          exit = pos
        }
        grid(pos) = cell
      }
      // On se déplace ensuite dans une direction aléatoire (mais valide)
      pos = randomValidNeighbour(pos, grid)
    }
    DungeonLevel(level, levelName(level), spawn, exit, grid)
  }

  private def generateCell(pos: Vec2i, spawn: Vec2i, tryExit: Boolean): Tile = {
    // Cas spécial : escalier de sortie, pas trop proche du spawn
    if (tryExit && pos.squaredDist(spawn) >= squaredMinExitDistance) {
      Tiles.StairsDown
    } else {
      Tiles.Void
    }
  }

  private def randomValidNeighbour(pos: Vec2i, grid: Grid[?]): Vec2i = {
    import WalkingGenerator._
    var next = Vec2i(-1, -1)
    while (!grid.isValid(next)) {
      next = pos + Directions.randomElement
    }
    next
  }

  private def levelName(level: Int): String = s"Level $level"
  // TODO find something better
}

object WalkingGenerator {
  import com.electronwill.why.util.Vec2i._
  private val Directions = Seq(Up, Right, Down, Left)
}
