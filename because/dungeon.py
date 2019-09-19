from geom import Point

class DungeonTerrain:
    def __init__(self, width: int, height: int):
        self._tiles = []*width*height
        self.xmax = width
        self.ymax = height

    def tiles(self):
        return iter(self._tiles)

    def __getitem__(self, pos):
        if isinstance(pos, Point):
            return self._tiles[pos.y*self.xmax + pos.x]
        else:
            return self._tiles[pos[1]*self.xmax + pos[0]]

    def __setitem__(self, pos, tile):
        if isinstance(pos, Point):
            self._tiles[pos.y*self.xmax + pos.x] = tile
        else:
            self._tiles[pos[1]*self.xmax + pos[0]] = tile


class DungeonLevel:
    def __init__(self, terrain, entities):
        self.terrain = terrain
        self.entities = entities
