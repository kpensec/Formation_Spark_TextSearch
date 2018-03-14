import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "org.formation",
      scalaVersion := "2.10.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "TextSearch",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scalactic" % "scalactic_2.10" % "3.0.5",
    libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "0.9.1"
  )
