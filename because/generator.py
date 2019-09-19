from dungeon import DungeonLevel
from geom import Point
import tiles

from random import randint

"""
Some dungeon generators
"""
# TODO: implement 2 generators


class BspGenerator:

    def __init__(self, xmax: int, ymax: int):
        self.xmax = xmax
        self.ymax = ymax

    def generate(self):
        pass  # TODO:


class WalkingGenerator:

    def __init__(self, xmax: int, ymax: int, fill_factor: float=0.75):
        self.xmax = xmax
        self.ymax = ymax
        self.fill_factor = fill_factor

    def generate(self):
        grid_size = self.xmax * self.ymax
        grid = [tiles.EMPTY] * grid_size
        entities = []  # TODO: generate entities (later)

        target = int(grid_size*self.fill_factor)  # round it down
        directions = [Point.UP, Point.DOWN, Point.RIGHT, Point.LEFT]
        pos = Point.random(self.xmax, self.ymax)
        i = 0
        while i < target:
            grid[pos] = tiles.STONE
            next_dir = directions[randint(0, 3)]

        return DungeonLevel(grid, entities)
