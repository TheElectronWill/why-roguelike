// See https://dotty.epfl.ch/docs/reference/contextual/extension-methods.html
import scala.util.Random

def (s: Seq[A]) randomElement[A]: A = s(Random.between(0, s.size)) 
