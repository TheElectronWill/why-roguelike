val dottyVersion = "0.19.0-RC1"

lazy val why = project
  .in(file("."))
  .settings(
    name := "why-game",
    version := "0.0.1",
    scalaVersion := dottyVersion,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
  .aggregate(
    shared,
    server,
    client
  )

lazy val shared = project
lazy val server = project.dependsOn(shared)
lazy val client = project.in(file("client-ascii")).dependsOn(shared)

