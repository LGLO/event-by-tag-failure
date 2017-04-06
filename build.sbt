lazy val root = (project in file("."))
  .settings(
    name := "events-by-tag-failure",
    scalaVersion := "2.11.8",
    cancelable in Global := true,
    fork in run := true,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-slf4j" % "2.4.17",
      "com.typesafe.akka" %% "akka-persistence" % "2.4.17",
      "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.25",
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "ch.qos.logback" % "logback-classic" % "1.2.2"
    )
  )