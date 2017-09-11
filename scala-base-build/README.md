[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fscala--base--build-008bb8.svg)](https://hub.docker.com/r/hbpmip/scala-base-build/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/scala-base-build.svg)](https://hub.docker.com/r/hbpmip/scala-base-build/tags "hbpmip/scala-base-build image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/scala-base-build.svg)](https://microbadger.com/#/images/hbpmip/scala-base-build "hbpmip/scala-base-build on microbadger")

# Base image for Scala programs

* hbpmip/scala-base-build

Image to use to build a Scala project. Contains Scala and sbt.

## Usage

Use this image as part of a multistage build:

Dockerfile
```
  FROM hbpmip/scala-base-build:3.5.0-jdk-8-7 as scala-build-env

  COPY build.sbt project/ src/ /build

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn clean package

  FROM hbpmip/java-base:8u131-1

  COPY --from=scala-build-env /build/target/scala_2.11/my-project.jar /usr/share/jars/

```
