[![CHUV](https://img.shields.io/badge/CHUV-LREN-AF4C64.svg)](https://www.unil.ch/lren/en/home.html) [![License](https://img.shields.io/badge/license-GPL--3.0-blue.svg)](https://github.com/LREN-CHUV/java-base-docker-images/blob/master/java-weka/LICENSE) [![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--weka-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-weka/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-weka.svg)](https://hub.docker.com/r/hbpmip/java-weka/tags "hbpmip/java-weka image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-weka.svg)](https://microbadger.com/#/images/hbpmip/java-weka "hbpmip/java-weka on microbadger")

# hbpmip/java-weka: Run Weka algorithms in MIP

Run Weka algorithms in MIP platform.

For the moment only classification algorithms...

## Usage

Use this image as the parent image to adapt a Weka algorithm to the MIP platform:

Dockerfile
```dockerfile
  FROM hbpmip/java-base-build:3.5.2-jdk-8-0 as build-java-env

  COPY pom.xml /project/pom.xml
  COPY src/ /project/src

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn package site

  FROM hbpmip/java-weka:0.2.6

  MAINTAINER <your email>

  ENV DOCKER_IMAGE=hbpmip/my-algo:1.0.0 \
      JAVA_CLASSPATH=${JAVA_CLASSPATH}:/opt/weka/props/:/usr/share/jars/my-algo.jar \
      JAVA_ARGS=/eu/humanbrainproject/mip/algorithms/weka/myalgo/settings.properties \
      MODEL=myalgo \
      FUNCTION=java-weka-myalgo

  COPY --from=build-java-env /project/target/my-algo.jar /usr/share/jars/my-algo.jar
  COPY --from=build-java-env /project/target/site/ /var/www/html/
  COPY src/ /src/

  RUN chown -R compute:compute /src/ \
      && chown -R root:www-data /var/www/html/
```

Your algorithm will require additional classes and files packaged in my-algo.jar to adapt it to MIP Woken and to generate PFA output:

* MyAlgoSerializer extends WekaClassifierSerializer<MyAlgo>
* settings.properties containing

```
classifier=weka.classifiers.functions.MyAlgo
classifierSerializer=eu.humanbrainproject.mip.algorithms.weka.simplelr.MyAlgoSerializer
```

We provide a Java library to support the integration:

```xml
<dependency>
  <groupId>eu.humanbrainproject.mip.algorithms</groupId>
  <artifactId>weka</artifactId>
  <version>${mip.weka.version}</version>
</dependency>
```
