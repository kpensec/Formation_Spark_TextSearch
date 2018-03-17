lazy val root = (project in file("."))
  .settings(
    name := "TextSearch",
    inThisBuild(List(
      organization := "org.formation",
      scalaVersion := "2.11.12",
      version      := "0.1.0-SNAPSHOT"
    )),

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.scalactic" %% "scalactic" % "3.0.5",
      "org.apache.spark" %% "spark-core" % "2.1.0",
      "org.apache.spark" %% "spark-sql" % "2.1.0"
    )
  )
