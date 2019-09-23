from math import sqrt
from random import randint

class Vec2D:
    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Vec2D(self.x+other.x, self.y+other.y)

    def __iadd__(self, other):
        self.x += other.x
        self.y += other.yfill_factor

    def __sub__(self, other):
        return Vec2D(self.x-other.x, self.y-other.y)

    def __isub__(self, other):
        self.x -= other.x
        self.y -= other.y

    def __mul__(self, k):
        return Vec2D(self.x*k, self.y*k)

    def __rmul__(self, k):
        return self*k  # calls __mul__

    def __imul__(self, k):
        self.x *= k
        self.y *= k

    def squaredDist(self, other):
        return (self.x-other.x)**2 + (self.y-other.y)**2

    def dist(self, other):
        return sqrt(self.squaredDist(other))

    @staticmethod
    def random(xmax: int, ymax: int):
        return Vec2D(randint(0, xmax), randint(0, ymax))


class Box:

    def __init__(self, xmin: int, ymin: int, xmax: int, ymax: int):
        self.xmin = xmin
        self.ymin = ymin
        self.xmax = xmax
        self.ymax = ymax

    def contains(self, p: Vec2D):
        return self.xmin <= p.x <= self.xmax and self.ymin <= p.y <= self.ymax

# Constantes
DOWN = Vec2D(0, 1)
UP = Vec2D(0, -1)
RIGHT = Vec2D(1, 0)
LEFT = Vec2D(-1, 0)
