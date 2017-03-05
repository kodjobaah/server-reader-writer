resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.bintrayRepo("sbt","sbt-plugin-releases")
resolvers += Resolver.url("bintray-sbt-plugins", url("https://dl.bintray.com/sbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
resolvers += Resolver.sbtPluginRepo("releases")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
