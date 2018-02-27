[![JSI](https://img.shields.io/badge/JSI-KT-AF4C64.svg)](http://kt.ijs.si/)
[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--jsi--clus-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-jsi-clus/)
[![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-jsi-clus.svg)](https://hub.docker.com/r/hbpmip/java-jsi-clus/tags "hbpmip/java-jsi-clus image tags")
[![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-jsi-clus.svg)](https://microbadger.com/#/images/hbpmip/java-jsi-clus "hbpmip/java-jsi-clus on microbadger")

# hbpmip/java-jsi-clus: Java environment for JSI CLUS algorithms

This image contains support for execution of CLUS software. CLUS contains multiple machine learning algorithms from JSI (http://kt.ijs.si).

## Usage

Use this image as part of a multistage build. You need to use this image as the parent image.

Dockerfile
```dockerfile
  FROM hbpmip/java-base-build:3.5.0-jdk-8-9 as build-java-env
  
  COPY pom.xml /project/pom.xml
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn dependency:resolve
  
  COPY src/ /project/src
  
  # Repeating copy of the settings works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn -Dmaven.test.skip=true package site
  
  FROM hbpmip/java-jsi-clus:latest
  
  MAINTAINER <your email>
  
  ENV DOCKER_IMAGE=my-algo \
      JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/my-algo.jar \
      JAVA_MAINCLASS=eu.humanbrainproject.mip.algorithms.jsi.clus.YourEntrypoint
  
  COPY --from=build-java-env /project/target/my-algo.jar /usr/share/jars/my-algo.jar
  COPY --from=build-java-env /project/target/site/ /var/www/html/
  COPY src/ /src/
  
  RUN chown -R compute:compute /src/ \
      && chown -R root:www-data /var/www/html/
  
  LABEL org.label-schema.build-date=$BUILD_DATE \
        org.label-schema.name="hbpmip/my-algo" \
        org.label-schema.description="My algorithm" \
        org.label-schema.url="https://github.com/LREN-CHUV/algorithm-repository" \
        org.label-schema.vcs-type="git" \
        org.label-schema.vcs-url="https://github.com/LREN-CHUV/algorithm-repository.git" \
        org.label-schema.vcs-ref=$VCS_REF \
        org.label-schema.version="$VERSION" \
        org.label-schema.vendor="JSI" \
        org.label-schema.license="GPLv3" \
        org.label-schema.docker.dockerfile="Dockerfile" \
        org.label-schema.schema-version="1.0"
```

Your algorithm will require additional classes and files packaged in my-algo.jar to adapt to the MIP Woken environment and to generate required outputs:

| Your class 	| Should extend  class 	| Description 	| Required 	|
|-------------------------------	|----------------------------------------	|--------------------------------------------------------------------------	|--------------------------------------	|
| YourAlgoMeta 	| ClusMeta 	| This class provides metadata of the algorithm. 	| Yes 	|
| YourAlgoSerializer 	| ClusGenericSerializer<ClusModel> 	| This class serializes your model to PFA. 	| Yes (if algorithm is predictive) 	|
| YourAlgoVisualizer 	| ClusVisualizationSerializer<ClusModel> 	| This class produces a visualization of your model. 	| No 	|
| YourAlgoDescriptiveSerializer 	| ClusDescriptionSerializer 	| This class produces a description of your model which is not predictive. 	| Yes (if algorithm is not predictive) 	|

We also provide a Java library to support the integration:

```xml
<repositories>
  <repository>
    <id>bintray-hbpmedical-mip</id>
    <name>bintray</name>
    <url>https://dl.bintray.com/hbpmedical/maven</url>
  </repository>
</repositories>

<dependency>
  <groupId>eu.humanbrainproject.mip.algorithms</groupId>
  <artifactId>jsi-clus</artifactId>
  <version>${mip.jsi-clus.version}</version>
</dependency>
```
