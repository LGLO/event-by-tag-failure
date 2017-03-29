lazy val root = (project in file("."))
  .settings(
    name := "events-by-tag-failure",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"        %% "akka-persistence"                  % "2.4.17",
      "com.typesafe.akka"        %% "akka-persistence-cassandra"        % "0.23"
    )
  )
