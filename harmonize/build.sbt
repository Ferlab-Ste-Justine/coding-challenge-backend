
name := "harmonize"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "Confluent Repository" at "https://packages.confluent.io/maven/"

libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.1.1"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.26"
libraryDependencies += "io.confluent" % "kafka-streams-avro-serde" % "5.1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "2.1.1" % Test

publishLocal := {}
publish := {}
crossPaths := false
assemblyJarName in assembly := s"${name.value}.jar"
