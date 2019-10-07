import mill._, scalalib._

// Dotty is the experimental project that will become Scala 3
trait DottyModule extends ScalaModule {
  def scalaVersion = "0.19.0-RC1"
}

// TODO depends on my libraries Niol and NightConfig
object shared extends DottyModule
object server extends DottyModule
object `client-ascii` extends DottyModule

