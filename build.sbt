name := """reactive-kafka"""

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.19",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.11",
  "ch.qos.logback" % "logback-classic" % "1.2.3")
