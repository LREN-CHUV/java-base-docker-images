
name          := "dummy"

version       := sys.env.getOrElse("VERSION", "dev")

scalaVersion  := "2.11.8"

val versions = new {
  val scalaTest = "2.2.5"
  val spec2 = "3.8.9"
}

libraryDependencies ++= {
  Seq(
  //---------- Test libraries -------------------//
    "org.scalatest"       %%  "scalatest"        % versions.scalaTest % "test",
    "org.specs2"          %%  "specs2-core"      % versions.spec2     % "test"
  )
}

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.8",
  "-encoding", "UTF-8"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

fork in Test := false

parallelExecution in Test := false

fork in run := true
