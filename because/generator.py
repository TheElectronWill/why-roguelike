from dungeon import DungeonLevel
from geom import Point
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
    def __init__(self, width: int, height: int, empty_factor: float=0.75):
        self.width = width
        self.height = height
        self.empty_factor = empty_factor

    def generate(self):
        grid_size = self.width * self.height
        grid = [tiles.EMPTY] * grid_size
        entities = []  # TODO: generate entities (later)

        target = int(grid_size*self.empty_factor)  # round it down
        directions = [Point.UP, Point.DOWN, Point.RIGHT, Point.LEFT]
        pos = Point.random(self.width, self.height)
        i = 0
        while i < target:
            grid[pos] = tiles.STONE
            # On va ensuite se déplacer dans une direction aléatoire
            # Tant que la nouvelle position est invalide, on recommence.
            next_pos = Point(-1, -1)
            while not (0 <= next_pos.x < self.width and 0 <= next_pos.y < self.height):
                next_pos = pos + directions[randint(0, 3)]
            # On met enfin à jour la position de façon valide
            pos = next_pos
        return DungeonLevel(grid, entities)
