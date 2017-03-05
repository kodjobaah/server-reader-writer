name := "reader"

enablePlugins(JavaAppPackaging)

javaOptions in Universal ++= Seq(
  "-Djava.library.path=/usr/local/lib"
)

net.virtualvoid.sbt.graph.Plugin.graphSettings

mainClass in (Compile, run) := Some("MainApp")
