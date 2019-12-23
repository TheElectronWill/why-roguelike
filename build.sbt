ThisBuild / scalaVersion := "0.21.0-RC1"

ThisBuild / scalacOptions += "-language:implicitConversions"

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

lazy val shared = project.settings(
  libraryDependencies += ("com.electronwill" %% "niol" % "2.0.1").withDottyCompat(scalaVersion.value).withSources(),
  libraryDependencies += ("com.electronwill.night-config" % "toml" % "3.6.2").withDottyCompat(scalaVersion.value).withSources()
)
lazy val server = project.dependsOn(shared)
lazy val client = project.in(file("client-ascii")).dependsOn(shared)
