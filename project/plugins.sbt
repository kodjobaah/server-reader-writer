resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M8")

addSbtPlugin("com.gilt" % "sbt-dependency-graph-sugar" % "0.7.5-1")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
addCommandAlias("generate-project", ";update-classifiers;gen-idea sbt-classifiers")

//addSbtPlugin("com.typesafe.sbt" % "sbt-aspectj" % "0.9.4")