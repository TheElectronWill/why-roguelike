from because.geom import Box
from because.listools import random_element, unordered_del

class BspTreeNode:
    def __init__(self, tree, box: Box):
        self.tree = tree
        self.box = box
        self.right, self.left = None, None
        tree.nodes.append(self)

    def is_leaf(self):
        assert (self.right is None) == (self.left is None), "That's not how you use a binary tree!"
        return self.right is None

    def split(self, up_down=None):
        assert self.right is None, "Node {} has already been split".format(self.box)
        box = self.box
        center = box.center()
        up_down = box.is_tall() if (up_down is None) else up_down
        if up_down:
            half_a = Box(box.xmin, center.y, box.xmax, box.ymax)
            half_b = Box(box.xmin, box.ymin, box.xmax, center.y)
        else:
            half_a = Box(center.x, box.ymin, box.xmax, box.ymax)
            half_b = Box(box.xmin, box.ymin, center.x, box.ymax)

        self.right, self.left = Node(half_a), Node(half_b)
        return self.right, self.left


class BspTree:
    """
    Arbre de partitionnement binaire de l'espace
    (Binary Space Partitioning Tree)
    """
    def __init__(self, box: Box):
        self.nodes = []
        self.root = BspTreeNode(self, box)

    def leafs(self):
        pass

    def nodes(self):
        pass


class BspGenerator:
    """
    Un générateur de niveau basé sur un arbre qui partitionne l'espace
    (Binary Space Partitioning Tree). L'idée est de diviser l'espace en deux
    de façon récursive et de créer une salle dans chaque feuille de l'arbre.
    Les salles qui ont le même noeud parent sont ensuite reliées par un chemin.

    :param: width nombre de colonnes de la grille
    :param: height nombre de lignes de la grille
    """
    def __init__(self, width: int, height: int,
                 split_count: int, min_room_dim: int):
        self.tree = BspTree(Box(0, 0, width, height))

    def is_big_enough(self, node: BspTreeNode):
        return node.box.dimension_min >= self.min_room_dim

    def can_split(self, node: BspTreeNode):
        return node.box.dimension_min >= self.min_room_dim*2

    def generate(self):
        tree = self.tree
        candidates = [tree.root]
        for _ in range(self.split_count):
            # Choose an element to split
            idx, chosen = random_element(candidates)
            if chosen is None:
                break

            # Split the node in half
            right, left = chosen.split()

            # Remove the parent node, and add the childs if they are big enough
            unordered_del(candidates, idx)  # O(1)
            if self.is_big_enough(right):
                candidates.append(right)
            if self.is_big_enough(left):
                candidates.append(left)

        for node in tree.nodes():
            if node.is_leaf():
                box = node.box
                # TODO: generate a room inside
                # offsetx, offsety = randint(min,max), ...
                # roomBox = Box(center-ox, center-oy, center+ox, center+oy)
            else:
                # create path right.center<->left.center
