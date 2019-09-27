from because.geom import Box


class BspTree:
    """
    Arbre de partitionnement binaire de l'espace
    (Binary Space Partitioning Tree)
    """
    def __init__(self):
        pass

    class Node:
        def __init__(self, box: Box):
            self.box = box
            self.right, self.left = None, None

        def split(self, up_down=None):
            box = self.box
            center = box.center()
            up_down = self.box.is_tall() if (up_down is None) else up_down
            if up_down:
                half_a = Box(box.xmin, center.y, box.xmax, box.ymax)
                half_b = Box(box.xmin, box.ymin, box.xmax, center.y)
            else:
                half_a = Box(center.x, box.ymin, box.xmax, box.ymax)
                half_b = Box(box.xmin, box.ymin, center.x, box.ymax)

            self.right, self.left = Node(half_a), Node(half_b)
            return self.right, self.left


class BspGenerator:
    """
    Un générateur de niveau basé sur un arbre qui partitionne l'espace
    (Binary Space Partitioning Tree). L'idée est de diviser l'espace en deux
    de façon récursive et de créer une salle dans chaque feuille de l'arbre.
    Les salles qui ont le même noeud parent sont ensuite reliées par un chemin.

    :param: width nombre de colonnes de la grille
    :param: height nombre de lignes de la grille
    """
    def __init__(self, width: int, height: int):
        self.width = width
        self.height = height

    def generate(self):
        pass  # TODO:
