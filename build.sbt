import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

name := "scala-healthcheck"

organization := "com.lonelyplanet"

version := "0.2.2-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype release repository" at "https://oss.sonatype.org/content/repositories/releases/"
)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion           = "2.4.8"
  val scalaJsonAPIVersion   = "0.5.0"
  val scalaUtilVersion      = "0.1.6"
  val scalaTestVersion      = "3.0.0-M15"

  val dependencies = Seq(
    "com.lonelyplanet"     %% "scala-util"                           % scalaUtilVersion
  )

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
    "com.typesafe.akka"    %% "akka-http-testkit"                    % akkaVersion % "test",
    "org.scalatest"        %% "scalatest"                            % scalaTestVersion % "test"
  )

  providedDependencies ++ testDependencies ++ dependencies
}

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(SpacesAroundMultiImports, false)
  .setPreference(CompactControlReadability, false)

bintrayOrganization := Some("lonelyplanet")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

val doNotPublishSettings = Seq(publish := {})

val publishSettings =
  if (version.toString.endsWith("-SNAPSHOT"))
    Seq(
      publishTo := Some("Artifactory Realm" at "http://oss.jfrog.org/artifactory/oss-snapshot-local"),
      bintrayReleaseOnPublish := false,
      credentials := List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
    )
  else
    Seq(
      organization := "com.lonelyplanet",
      pomExtra := <scm>
        <url>https://github.com/lonelyplanet/scala-healthcheck</url>
        <connection>https://github.com/lonelyplanet/scala-healthcheck</connection>
      </scm>
        <developers>
          <developer>
            <id>wlk</id>
            <name>Wojciech Langiewicz</name>
            <url>https://github.com/lonelyplanet/scala-healthcheck</url>
          </developer>
        </developers>,
      publishArtifact in Test := false,
      homepage := Some(url("https://github.com/lonelyplanet/scala-healthcheck")),
      publishMavenStyle := false,
      resolvers += Resolver.url("lonelyplanet ivy resolver", url("http://dl.bintray.com/lonelyplanet/maven"))(Resolver.ivyStylePatterns)
    )