[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--build--base-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-build-base/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-build-base.svg)](https://hub.docker.com/r/hbpmip/java-build-base/tags "hbpmip/java-build-base image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-build-base.svg)](https://microbadger.com/#/images/hbpmip/java-build-base "hbpmip/java-build-base on microbadger")

# hbpmip/java-build-base: Java build environment, including Maven

This image contains the Java JDK and Maven 3 (from [Maven image](https://hub.docker.com/r/_/maven/))

## Usage

Use this image as part of a multistage build:

Dockerfile
```
  FROM hbpmip/java-build-base:3.5.0-jdk-8-1 as java-build-env

  COPY pom.xml /project/
  COPY src/ /project/src/

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn clean package

  FROM hbpmip/java-base:8u131-1

  COPY --from=build-java-env /project/target/my-project.jar /usr/share/jars/

```
