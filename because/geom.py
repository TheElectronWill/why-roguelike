from math import sqrt
from random import randint

class Point:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Point(self.x+other.x, self.y+other.y)

    def __iadd__(self, other):
        self.x += other.x
        self.y += other.y

    def __sub__(self, other):
        return Point(self.x-other.x, self.y-other.y)

    def __isub__(self, other):
        self.x -= other.x
        self.y -= other.y

    def squaredDist(self, other):
        return (self.x-other.x)**2 + (self.y-other.y)**2

    def dist(self, other):
        return sqrt(self.squaredDist(other))

    @staticmethod
    def random(xmax: int, ymax: int):
        return Point(randint(0, xmax), randint(0, ymax))


class Box:

    def __init__(self, xmin: int, ymin: int, xmax: int, ymax: int):
        self.xmin = xmin
        self.ymin = ymin
        self.xmax = xmax
        self.ymax = ymax

    def contains(self, p: Point):
        return self.xmin <= p.x <= self.xmax and self.ymin <= p.y <= self.ymax

# Constants
DOWN = Point(0, 1)
UP = Point(0, -1)
RIGHT = Point(1, 0)
LEFT = Point(-1, 0)
