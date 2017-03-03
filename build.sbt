name := "txodds"

version := "1.0"

scalaVersion := "2.11.7"

conflictWarning := ConflictWarning.disable

net.virtualvoid.sbt.graph.Plugin.graphSettings

enablePlugins(JavaAppPackaging)

resolvers ++= Seq("Spray repository" at "http://repo.spray.io/",
"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
"Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

resolvers += Resolver.mavenLocal
resolvers += DefaultMavenRepository


libraryDependencies ++= {
  Seq(
    "net.java.dev.jna"    %   "jna"                       % "3.4.0",
    "com.github.jnr"      %   "jnr-constants"             % "0.8.2",
    "org.apache.commons"  %   "commons-io"                % "1.3.2",
    "commons-logging"     %   "commons-logging"           % "1.1.1",
    "org.greencheek.spray"%   "spray-cache-spymemcached"  % "0.1.6",
    "org.ostermiller"     %   "utils"                     % "1.07.00",
    "org.apache.commons"  %   "commons-lang3"             % "3.0",
    "commons-cli"         %   "commons-cli"               % "1.2",
    "org.slf4j"           %   "slf4j-api"                 % "1.7.24",
    "ch.qos.logback"      %   "logback-classic"           % "1.0.13",
    "org.mindrot"         %   "jbcrypt"                   % "0.3m",
    "org.apache.commons"  %   "commons-email"             % "1.3.1",
  "org.aspectj" % "aspectjweaver" % "1.8.1"
  )

}


//aspectjSettings

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

parallelExecution in Test := false

Revolver.settings: Seq[sbt.Setting[_]]

javaOptions in Universal ++= Seq(
"-javaagent:/Users/baahko01/personl/txodds/lib/weaver/aspectjweaver-1.8.6.jar",
"-Dorg.aspectj.tracing.factory=default",
"-Djava.library.path=/usr/local/lib"
)


fork in run := true

connectInput in run := true

outputStrategy in run := Some(StdoutOutput)

net.virtualvoid.sbt.graph.Plugin.graphSettings

mainClass in (Compile, run) := Some("MainApp")

//Revolver.settings.settings

