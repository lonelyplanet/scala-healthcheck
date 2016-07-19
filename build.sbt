import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

name := "scala-healthcheck"

version := "0.1"

scalaVersion := "2.11.8"

resolvers += "Sonatype release repository" at "https://oss.sonatype.org/content/repositories/releases/"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion           = "2.4.8"
  val scalaJsonAPIVersion   = "0.5.0"

  val providedDependencies = Seq(
    "com.typesafe.akka"    %% "akka-actor"                           % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-slf4j"                           % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-stream"                          % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-http-core"                       % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-http-experimental"               % akkaVersion % "provided",
    "com.typesafe.akka"    %% "akka-http-spray-json-experimental"    % akkaVersion % "provided",
    "org.zalando"          %% "scala-jsonapi"                        % scalaJsonAPIVersion % "provided"
  )

  val testDependencies = Seq(
      "com.typesafe.akka"    %% "akka-http-testkit"                    % akkaVersion % "test"
    )

  providedDependencies ++ testDependencies
}

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(CompactControlReadability, false)
