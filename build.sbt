organization := "gpio4s"
name := "gpiocfg"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.5"
scalacOptions += "-target:jvm-1.8"

libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.3.0",
    "net.ceedubs" % "ficus_2.11" % "1.1.2",
    "org.scalatest" % "scalatest_2.11" % "2.2.5" % Test
)
