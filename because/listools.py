from typing import Callable, Iterable
from random import randint


def lfilter(f: Callable, i: Iterable):
    return list(filter(f, i))


def random_element(l: list):
    max = len(l)-1
    if max < 0:
        return None, None
    idx = randint(0, max)
    return idx, l[idx]


def unordered_del(l: list, idx: int):
    l[idx] = l[-1]
    del l[-1]
    # return None
