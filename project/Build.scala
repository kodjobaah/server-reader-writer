import sbt._
import Keys._

object Dependencies {
  val apacheCommonsIo = "org.apache.commons"  %   "commons-io"                % "1.3.2"
  val apacheCommonsLogging = "commons-logging"     %   "commons-logging"           % "1.1.1"
  val apacheCommonsLang = "org.apache.commons"  %   "commons-lang3"             % "3.0"
  val apacheCommonsCollections = "org.apache.commons" % "commons-collections4" % "4.1"
  val apacheCommonsCli = "commons-cli"         %   "commons-cli"               % "1.2"
  val apacheSlf4 = "org.slf4j"           %   "slf4j-api"                 % "1.7.24"
  val guava = "com.google.guava" % "guava" % "21.0"
  val apacheLogback = "ch.qos.logback"      %   "logback-classic"           % "1.0.13"
  val redis =  "net.debasishg" %% "redisclient" % "3.3"
  val mbeans = "com.tzavellas" % "sse-jmx" % "0.4.1"
  val scalaTest =  "org.scalatest" % "scalatest_2.11" % "3.0.1"


}

object TxOddsBuild extends Build {

  lazy val defaultSettings =
    Defaults.coreDefaultSettings ++
      Seq(
        version := "1.0",
        scalaVersion := "2.11.7",
        scalacOptions := Seq(
          "-feature",
          "-language:implicitConversions",
          "-language:postfixOps",
          "-unchecked",
          "-deprecation",
          "-encoding", "utf8",
          "-Ywarn-adapted-args"
        )
      )

  import Dependencies._
  val commonDeps = Seq (
    apacheCommonsIo,
    apacheCommonsLogging,
    apacheCommonsLang,
    apacheCommonsCli,
    apacheSlf4,
    apacheLogback,
    apacheCommonsCollections,
    guava,
    redis,
    mbeans,
    scalaTest
  )

  val commonResolvers = Seq("Spray repository" at "http://repo.spray.io/",
    "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
  )

  lazy val root = (project in file(".")).aggregate(server, reader, writer, core)

  lazy val core = (project in file("core"))
    .settings(libraryDependencies ++= commonDeps:_*)
    .settings(defaultSettings.settings: _*)
    .settings(resolvers ++= commonResolvers:_*)

  lazy val server = (project in file("server")).settings(libraryDependencies ++= commonDeps:_*)
     //   .settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6"):_*)
        .settings(defaultSettings.settings: _*)
        .settings(resolvers ++= commonResolvers:_*).dependsOn(core)

  lazy val writer = (project in file("writer")).settings(libraryDependencies ++= commonDeps:_*)
    //.settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6"):_*)
    .settings(defaultSettings.settings: _*)
    .settings(resolvers ++= commonResolvers:_*).dependsOn(core)

  lazy val reader = (project in file("reader")).settings(libraryDependencies ++= commonDeps:_*)
    //.settings(addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6"):_*)
    .settings(defaultSettings.settings: _*)
    .settings(resolvers ++= commonResolvers:_*).dependsOn(core)
}
