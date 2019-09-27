from dungeon import DungeonLevel
from geom import Vec2D, UP, DOWN, RIGHT, LEFT
import tiles

from random import randint

"""
Some dungeon generators
"""
# TODO: implement 2 generators


class BspGenerator:
    def __init__(self, width: int, height: int):
        self.width = width
        self.height = height

    def generate(self):
        pass  # TODO:


class WalkingGenerator:
    """
    Un générateur de niveau basé sur de la marche aléatoire.
    Le principe est simple : on commence avec une grille remplie et une position
    aléatoire sur cette grille. On vide cette case puis on se déplace dans une
    direction aléatoire, et on recommence.

    :param: width nombre de colonnes de la grille
    :param: height nombre de lignes de la grille
    :param: fill_factor pourcentage de cases à vider, par défaut 0.75
    """
    def __init__(self, width: int, height: int,
                 empty_factor=0.75, min_exit_distance=2.0):
        self.width = width
        self.height = height
        self.empty_factor = empty_factor
        self.min_exit_distance = min_exit_distance

    def _is_valid(self, position: Vec2D):
        return 0 <= position.x <= self.width and 0 <= position.y < self.height

    def generate(self):
        grid_size = self.width * self.height
        grid = [tiles.STONE] * grid_size
        entities = []  # TODO: generate entities (later)

        spawn = Vec2D.random(self.width, self.height)
        exit = None

        squared_exit_dist = self.min_exit_distance**2  # optimization
        target = int(grid_size*self.empty_factor)  # rounded down
        directions = [UP, DOWN, RIGHT, LEFT]
        pos, i = spawn, 0
        while i < target:
            # On vide la cellule courante
            if grid[pos] != tiles.EMPTY:
                # Cas spécial : escalier de sortie, pas trop proche du spawn
                if not exit and pos.squaredDist(spawn) >= squared_exit_dist:
                    grid[pos] = tiles.STAIRS_DOWN
                    exit = pos
                else:
                    grid[pos] = tiles.EMPTY
                i += 1

            # On se déplace ensuite dans une direction aléatoire (mais valide).
            next_pos = Vec2D(-1, -1)
            while not self._is_valid(next_pos):
                next_pos = pos + directions[randint(0, 3)]
            pos = next_pos

        return DungeonLevel(grid, entities, spawn, exit)
