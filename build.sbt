ThisBuild / scalaVersion := "0.20.0-RC1"

lazy val why = project
  .in(file("."))
  .settings(
    name := "why-game",
    version := "0.0.1",
    libraryDependencies += ("com.electronwill" %% "more-collections" % "1.0.0").withDottyCompat(scalaVersion.value),
    libraryDependencies += ("com.electronwill" %% "niol" % "2.0.0").withDottyCompat(scalaVersion.value)
  )
  .aggregate(
    shared,
    server,
    client
  )

lazy val shared = project
lazy val server = project.dependsOn(shared)
lazy val client = project.in(file("client-ascii")).dependsOn(shared)
