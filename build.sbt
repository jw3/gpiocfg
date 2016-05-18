organization := "com.github.jw3"
name := "gpiocfg"
version := "0.2-SNAPSHOT"
licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.11.8"
scalacOptions += "-target:jvm-1.8"

resolvers += "jw3 at bintray" at "https://dl.bintray.com/jw3/maven"

libraryDependencies ++= {
    val scalatestVersion = "3.0.0-M15"

    Seq(
        "com.typesafe" % "config" % "1.3.0",
        "net.ceedubs" %% "ficus" % "1.1.2",

        "org.scalactic" %% "scalactic" % scalatestVersion % Test,
        "org.scalatest" %% "scalatest" % scalatestVersion % Test
    )
}
