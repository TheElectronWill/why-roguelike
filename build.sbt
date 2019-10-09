ThisBuild / scalaVersion := "0.19.0-RC1"

lazy val why = project
  .in(file("."))
  .settings(
    name := "why-game",
    version := "0.0.1"
  )
  .aggregate(
    shared,
    server,
    client
  )

lazy val shared = project
lazy val server = project.dependsOn(shared)
lazy val client = project.in(file("client-ascii")).dependsOn(shared)

