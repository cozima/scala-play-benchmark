
name := """scala-play-benchmark"""
maintainer := """your.name@company.org"""
version := "1.0-SNAPSHOT"

scalaVersion := "2.12.8"

lazy val GatlingTest = config("gatling") extend Test


lazy val root = (project in file("."))
  .aggregate(api, gatling)

lazy val api = project
  .settings(
    name := "api",
    libraryDependencies ++= Seq(
      guice,
      "com.fasterxml.uuid"     % "java-uuid-generator"   % "3.2.0",
      "io.circe"               %% "circe-core"           % "0.11.1",
      "io.circe"               %% "circe-parser"         % "0.11.1",
      "io.circe"               %% "circe-literal"        % "0.11.1",
      "io.circe"               %% "circe-generic"        % "0.11.1",
      "io.circe"               %% "circe-generic-extras" % "0.11.1",
      "io.circe"               %% "circe-java8"          % "0.11.1",
      "com.github.xuwei-k"     % "msgpack4z-api"         % "0.2.0",
      "com.github.xuwei-k"     %% "msgpack4z-core"       % "0.3.9",
      "com.github.xuwei-k"     %% "msgpack4z-circe"      % "0.8.0",
      "com.github.xuwei-k"     %% "msgpack4z-native"     % "0.3.5",
      "software.amazon.awssdk" % "dynamodb"              % "2.6.4"
    )
  )
  .enablePlugins(PlayScala)

lazy val gatling = project
  .settings(
    name := "gatling",
    libraryDependencies ++= Seq(
      "io.gatling"             % "gatling-test-framework"    % "3.0.3" % Test,
      "io.gatling.highcharts"  % "gatling-charts-highcharts" % "3.0.3" % Test
    )
  )
  .enablePlugins(GatlingPlugin)
  .dependsOn(api)
