[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fscala--base--build-008bb8.svg)](https://hub.docker.com/r/hbpmip/scala-base-build/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/scala-base-build.svg)](https://hub.docker.com/r/hbpmip/scala-base-build/tags "hbpmip/scala-base-build image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/scala-base-build.svg)](https://microbadger.com/#/images/hbpmip/scala-base-build "hbpmip/scala-base-build on microbadger")

# Base image for Scala programs

* hbpmip/scala-base-build

Image to use to build a Scala project. Contains Scala and sbt.

## Usage

Use this image as part of a multistage build:

Dockerfile
```dockerfile
  FROM hbpmip/scala-base-build:0.13.16-6 as scala-build-env

  COPY build.sbt /build/
  COPY project/ /build/project/

  # Run sbt on an empty project and force it to download most of its dependencies to fill the cache
  RUN sbt compile

  COPY src/ /build/src/
  COPY .git/ /build/.git/
  # Adapt this line to your project
  COPY .circleci/ /build/.circleci/
  # Adapt this line to your project
  COPY .*.cfg .*ignore .*.yaml .*.conf *.md *.builder *.sh *.yml *.json LICENSE /build/

  # Check that all sources have been copied with the .git repository, to avoid missing files and snapshot versions during publication
  RUN /check-sources.sh

  RUN sbt package

  FROM hbpmip/java-base:8u151-0

  COPY --from=scala-build-env /build/target/scala_2.11/my-project.jar /usr/share/jars/

```

Environment variables:

* BINTRAY_USER and BINTRAY_PASS: Credentials for BinTray. See [sbt-bintray](https://github.com/sbt/sbt-bintray)

# Scala development

## Recommended global sbt plugins

Add to ~/.sbt/0.13/plugins/sbt-updates.sbt

```scala
  addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5") // stats

  addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2") // dependencyGraph

  addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.3") // dependencyUpdates

  addSbtPlugin("com.github.xuwei-k" % "sbt-class-diagram" % "0.1.7")

  // Faster development
  addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

  // Benchmarking
  addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.27")

  addSbtPlugin("com.updateimpact" % "updateimpact-sbt-plugin" % "2.1.1")

```

Other sbt plugins:

```
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.3.0")
```
