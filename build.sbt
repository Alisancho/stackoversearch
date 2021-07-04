import sbtassembly.MergeStrategy

name := "stackoversearch"

version := "0.1"

scalaVersion := "2.13.6"

lazy val akkaVersion    = "2.6.15"
lazy val akkaHttp       = "10.2.4"
lazy val softwaremill   = "2.3.7"
lazy val logBackVersion = "1.2.3"

mainClass in assembly := Some("ru.stackoversearch.AppStart")

assemblyMergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x)                                                      =>
    MergeStrategy.concat
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
    MergeStrategy.rename
  case PathList("META-INF", xs @ _*)                                                      =>
    (xs map {
      _.toLowerCase
    }) match {
      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
        MergeStrategy.discard
      case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa")    =>
        MergeStrategy.discard
      case "plexus" :: xs                                                           =>
        MergeStrategy.discard
      case "services" :: xs                                                         =>
        MergeStrategy.filterDistinctLines
      case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil)                   =>
        MergeStrategy.filterDistinctLines
      case _                                                                        => MergeStrategy.first
    }
  case _                                                                                  => MergeStrategy.first
}
// https://mvnrepository.com/artifact/org.typelevel/cats-core
libraryDependencies += "org.typelevel" %% "cats-core" % "2.6.1"

libraryDependencies ++= Seq(
  ("com.typesafe.akka" %% "akka-stream"          % akkaVersion),
  ("com.typesafe.akka" %% "akka-http"            % akkaHttp),
  ("com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp),
  ("com.typesafe.akka" %% "akka-http-caching"    % akkaHttp),
  ("com.typesafe.akka" %% "akka-slf4j"           % akkaVersion)
)

libraryDependencies ++= Seq(
//  "com.softwaremill.macwire"      %% "util"                           % softwaremill,
  "com.softwaremill.sttp"         %% "async-http-client-backend-cats" % "1.7.2",
  "com.softwaremill.sttp.client3" %% "akka-http-backend"              % "3.3.9",
  "com.typesafe.scala-logging"    %% "scala-logging"                  % "3.9.3",
  "ch.qos.logback"                 % "logback-classic"                % logBackVersion,
  "ch.qos.logback"                 % "logback-core"                   % logBackVersion,
  "net.logstash.logback"           % "logstash-logback-encoder"       % "6.2"
)
