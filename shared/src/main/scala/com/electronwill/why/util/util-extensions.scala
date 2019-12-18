package com.electronwill.why.util

// See https://dotty.epfl.ch/docs/reference/contextual/extension-methods.html
import scala.util.Random

def [A](s: Seq[A]) randomElement: A = s(Random.between(0, s.size))

def [A,B](t: (A,A,A,A)) tmap(f: A => B): (B,B,B,B) = (f(t._1), f(t._2), f(t._3), f(t._4))
