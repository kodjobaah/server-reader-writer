name := "writer"

enablePlugins(JavaAppPackaging)

javaOptions in Universal ++= Seq(
"-Djava.library.path=/usr/local/lib"
)

mainClass in (Compile, run) := Some("MainApp")


