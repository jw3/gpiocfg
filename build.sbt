organization := "gpio4s"
name := "gpiocfg"
version := "0.2-SNAPSHOT"
licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.11.5"
scalacOptions += "-target:jvm-1.8"

resolvers += "jw3 at bintray" at "https://dl.bintray.com/jw3/maven"
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")

libraryDependencies ++= Seq(
    "com.typesafe" % "config" % "1.3.0",
    "net.ceedubs" % "ficus_2.11" % "1.1.2",
    "org.scalatest" % "scalatest_2.11" % "2.2.5" % Test
)
