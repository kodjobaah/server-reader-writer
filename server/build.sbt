name := "server"

enablePlugins(JavaAppPackaging)

javaOptions in Universal ++= Seq(
  "-Djava.library.path=/usr/local/lib",
    "-Dcom.sun.management.jmxremote",
    "-Dcom.sun.management.jmxremote.port=1617",
    "-Dcom.sun.management.jmxremote.authenticate=false",
    "-Dcom.sun.management.jmxremote.ssl=false"
)

mainClass in (Compile, run) := Some("MainApp")
