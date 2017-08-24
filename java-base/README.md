[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--base-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-base/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-base.svg)](https://hub.docker.com/r/hbpmip/java-base/tags "hbpmip/java-base image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-base.svg)](https://microbadger.com/#/images/hbpmip/java-base "hbpmip/java-base on microbadger")

# hbpmip/java-base: Base image for Java programs

This image contains the base Java Runtime (from [OpenJDK image](https://hub.docker.com/r/_/openjdk/))

## Usage

Dockerfile
```
  FROM hbpmip/java-base:8u131-1

```


* hbpmip/java-base-build

Image to use to build a Java project. Contains the JDK and Maven 3.
