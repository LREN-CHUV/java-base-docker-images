[![CHUV](https://img.shields.io/badge/CHUV-LREN-AF4C64.svg)](https://www.unil.ch/lren/en/home.html) [![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](https://github.com/LREN-CHUV/java-base-docker-images/blob/master/LICENSE) [![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--base--build-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-base-build/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-base-build.svg)](https://hub.docker.com/r/hbpmip/java-base-build/tags "hbpmip/java-base-build image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-base-build.svg)](https://microbadger.com/#/images/hbpmip/java-base-build "hbpmip/java-base-build on microbadger")

# hbpmip/java-base-build: Java build environment, including Maven

This image contains the Java JDK and Maven 3 (from [Maven image](https://hub.docker.com/r/_/maven/))

## Usage

Use this image as part of a multistage build:

Dockerfile
```dockerfile
  FROM hbpmip/java-base-build:3.5.2-jdk-8-0 as java-build-env

  COPY pom.xml /project/
  COPY src/ /project/src/

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn clean package

  FROM hbpmip/java-base:11.0.1-0

  COPY --from=java-build-env /project/target/my-project.jar /usr/share/jars/

```
