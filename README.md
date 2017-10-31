[![CHUV](https://img.shields.io/badge/CHUV-LREN-AF4C64.svg)](https://www.unil.ch/lren/en/home.html) [![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](https://github.com/LREN-CHUV/java-base-docker-images/blob/master/LICENSE) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/978ae82bbdb540129ee8a82e3b8ed21d)](https://www.codacy.com/app/hbp-mip/java-base-docker-images?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=HBPMedical/java-base-docker-images&amp;utm_campaign=Badge_Grade)
[![CircleCI](https://circleci.com/gh/LREN-CHUV/java-base-docker-images.svg?style=svg)](https://circleci.com/gh/LREN-CHUV/java-base-docker-images)

# java-base-docker-images

Set of Docker images for programs and algorithms using Java.

## [hbpmip/java-base](./java-base/): Base image for Java programs

[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--base-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-base/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-base.svg)](https://hub.docker.com/r/hbpmip/java-base/tags "hbpmip/java-base image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-base.svg)](https://microbadger.com/#/images/hbpmip/java-base "hbpmip/java-base on microbadger")

This image contains the base Java Runtime (from [OpenJDK image](https://hub.docker.com/r/_/openjdk/))

## [hbpmip/java-base-build](./java-base-build/): Java build environment, including Maven

[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--base--build-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-base-build/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-base-build.svg)](https://hub.docker.com/r/hbpmip/java-base-build/tags "hbpmip/java-base-build image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-base-build.svg)](https://microbadger.com/#/images/hbpmip/java-base-build "hbpmip/java-base-build on microbadger")

This image contains the Java JDK and Maven 3 (from [Maven image](https://hub.docker.com/r/_/maven/))

Use it to build Java projects.

## [hbpmip/java-mip](./java-mip/): Adapt the base Java image to the MIP environment

[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--mip-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-mip/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-mip.svg)](https://hub.docker.com/r/hbpmip/java-mip/tags "hbpmip/java-mip image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-mip.svg)](https://microbadger.com/#/images/hbpmip/java-mip "hbpmip/java-mip on microbadger")

This image provides a Java environment compatible with MIP. This base image provides the
basic tools and library to execute a statistical or machine-learning Java algorithm in the runtime
environment provided by MIP Algorithm Factory / Woken.

## [hbpmip/java-rapidminer](./java-rapidminer/): Run RapidMinder algorithms in MIP

[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--rapidminer-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-rapidminer/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-rapidminer.svg)](https://hub.docker.com/r/hbpmip/java-rapidminer/tags "hbpmip/java-rapidminer image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-rapidminer.svg)](https://microbadger.com/#/images/hbpmip/java-rapidminer "hbpmip/java-rapidminer on microbadger")

Run Rapidminer algorithms (experiments) in MIP platform.

## [hbpmip/java-weka](./java-weka/): Run Weka algorithms in MIP

[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--weka-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-weka/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-weka.svg)](https://hub.docker.com/r/hbpmip/java-weka/tags "hbpmip/java-weka image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-weka.svg)](https://microbadger.com/#/images/hbpmip/java-weka "hbpmip/java-weka on microbadger")

Run Weka algorithms (cliassifiers) in MIP platform.

## [hbpmip/scala-base-build](./scala-base-build/): Scala build environment, including sbt

[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fscala--base--build-008bb8.svg)](https://hub.docker.com/r/hbpmip/scala-base-build/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/scala-base-build.svg)](https://hub.docker.com/r/hbpmip/scala-base-build/tags "hbpmip/scala-base-build image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/scala-base-build.svg)](https://microbadger.com/#/images/hbpmip/scala-base-build "hbpmip/scala-base-build on microbadger")

This image contains [Scala](https://www.scala-lang.org/) and [sbt](http://www.scala-sbt.org/)

Use it to build Scala projects.
