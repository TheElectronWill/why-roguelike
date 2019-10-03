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

    def squaredNorm(self):
        return self.x**2 + self.y**2

    def norm(self):
        return sqrt(self.squaredNorm())

    def squaredDist(self, other):
        return (self.x-other.x)**2 + (self.y-other.y)**2

    def dist(self, other):
        return sqrt(self.squaredDist(other))

    def snakeNorm(self):
        """Computes the snake/manhattan norm of this vector"""
        return abs(self.x) + abs(self.y)

    def snakeDist(self, other):
        """
        Computes the snale/manhattan distance between this (vector considered
        as a) point and another one.
        """
        return abs(self.x-other.x) + abs(self.y-other.y)

    @staticmethod
    def random(xmax: int, ymax: int, including: bool=False):
        if including:
            xmax, ymax = xmax+1, ymax+1
        return Vec2D(randint(0, xmax), randint(0, ymax))

# Constantes
Vec2D.DOWN = Vec2D(0, 1)
Vec2D.UP = Vec2D(0, -1)
Vec2D.RIGHT = Vec2D(1, 0)
Vec2D.LEFT = Vec2D(-1, 0)


class Box:
    def __init__(self, xmin: int, ymin: int, xmax: int, ymax: int):
        self.xmin = xmin
        self.ymin = ymin
        self.xmax = xmax
        self.ymax = ymax

    def contains(self, p: Vec2D):
        return self.xmin <= p.x <= self.xmax and self.ymin <= p.y <= self.ymax

    def width(self):
        return self.xmax - self.xmin

    def height(self):
        return self.ymax - self.ymin

    def dimension_min(self):
        return min(self.width(), self.height())

    def dimension_max(self):
        return max(self.width(), self.height())

    def corner_min(self):
        return Vec2D(self.xmin, self.ymin)

    def corner_max(self):
        return Vec2D(self.xmax, self.ymax)

    def center(self):
        return Vec2D(self.width()//2, self.height()//2)

    def real_center(self):
        return (self.width()/2, self.height()/2)

    def is_tall(self):
        return self.height() > self.width()

    def is_wide(self):
        return self.height() < self.width()

    def is_square(self):
        return self.height() == self.width()

    def __str__(self, arg):
        return "[{}, {}]x[{}, {}]".format(self.xmin, self.ymin, self.xmax, self.ymax)
