
name          := "seed"

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val scalaCheck = "1.14.0"
      val scalaTest  = "3.0.5"
      val akka       = "2.5.19"
      val akkaHttp   = "10.1.7"
      val sprayJson  = "1.3.5"
      val slf4j      = "1.7.25"
      val log4j      = "2.11.1"
      val cats       = "1.5.0"
    }
    val scalaCheck: ModuleID       = "org.scalacheck"    %% "scalacheck"   % Version.scalaCheck
    val scalaTest: ModuleID        = "org.scalatest"     %% "scalatest"    % Version.scalaTest
    val akkaActor: ModuleID        = "com.typesafe.akka" %% "akka-actor"   % Version.akka
    val akkaRemote: ModuleID       = "com.typesafe.akka" %% "akka-remote"  % Version.akka
    val akkaCluster: ModuleID      = "com.typesafe.akka" %% "akka-cluster" % Version.akka
    val akkaClusterTools: ModuleID = "com.typesafe.akka" %% "akka-cluster-tools" % Version.akka
    val akkaStream: ModuleID       = "com.typesafe.akka" %% "akka-stream"  % Version.akka
    val akkaContrib: ModuleID      = "com.typesafe.akka" %% "akka-contrib" % Version.akka
    val akkaSlf4j: ModuleID        = "com.typesafe.akka" %% "akka-slf4j"   % Version.akka
    val akkaTestkit: ModuleID      = "com.typesafe.akka" %% "akka-testkit" % Version.akka
    val akkaStreamTestkit: ModuleID = "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka
    val akkaHttp: ModuleID         = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
    val akkaHttpJson: ModuleID     = "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttp
    val sprayJson: ModuleID        = "io.spray"          %% "spray-json"   % Version.sprayJson
    val slf4j: ModuleID            = "org.slf4j"          % "slf4j-api"    % Version.slf4j
    val log4jSlf4j: ModuleID       = "org.apache.logging.log4j" % "log4j-slf4j-impl" % Version.log4j
    val catsCore: ModuleID         = "org.typelevel"     %% "cats-core"    % Version.cats
  }

lazy val `seed` =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitVersioning, GitBranchPrompt)
    .settings(settings)
    .settings(
      Seq(
        libraryDependencies ++= Seq(
          library.akkaActor,
          library.akkaRemote,
          library.akkaCluster,
          library.akkaClusterTools,
          library.akkaStream,
          library.akkaContrib,
          library.akkaSlf4j,
          library.akkaHttp,
          library.akkaHttpJson,
          library.sprayJson,
          library.slf4j,
          library.log4jSlf4j,
          library.catsCore,
          library.scalaCheck % Test,
          library.scalaTest  % Test,
          library.akkaTestkit  % Test,
          library.akkaStreamTestkit  % Test
        ),
        crossScalaVersions := Seq("2.11.12", "2.12.8")
      )
    )

lazy val settings = commonSettings ++ gitSettings ++ scalafmtSettings

lazy val commonSettings =
  Seq(
    organization := "eu.humanbrainproject.mip",
    organizationName := "LREN CHUV",
    startYear := Some(2017),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-value-discard",
      "-Ypartial-unification",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8"
    ),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    wartremoverWarnings in (Compile, compile) ++= Warts.unsafe,
    fork in run := true,
    test in assembly := {},
    fork in Test := false,
    parallelExecution in Test := false
  )

lazy val gitSettings =
  Seq(
    git.gitTagToVersionNumber := { tag: String =>
      if (tag matches "[0-9]+\\..*") Some(tag)
      else None
    },
    git.useGitDescribe := true
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtOnCompile.in(Sbt) := false,
    scalafmtVersion := "1.5.1"
  )
