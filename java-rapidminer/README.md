[![CHUV](https://img.shields.io/badge/CHUV-LREN-AF4C64.svg)](https://www.unil.ch/lren/en/home.html) [![License](https://img.shields.io/badge/license-AGPL--3.0-blue.svg)](https://github.com/LREN-CHUV/java-base-docker-images/blob/master/java-rapidminer/LICENSE) [![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--rapidminer-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-rapidminer/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-rapidminer.svg)](https://hub.docker.com/r/hbpmip/java-rapidminer/tags "hbpmip/java-rapidminer image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-rapidminer.svg)](https://microbadger.com/#/images/hbpmip/java-rapidminer "hbpmip/java-rapidminer on microbadger")

# hbpmip/java-rapidminer: Run RapidMinder algorithms in MIP

Run Rapidminer algorithms (experiments) in MIP platform.

For the moment only classification algorithms...

It can run any RapidMiner experiments (based on classifier) as long as the .rmp template is present
in the data/in/template volume folder

## Usage

Use this image as the parent image to adapt a RapidMiner algorithm to the MIP platform:

Dockerfile
```dockerfile
  FROM hbpmip/java-base-build:3.5.2-jdk-8-0 as build-java-env

  COPY pom.xml /project/pom.xml
  COPY src/ /project/src

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn package site

  FROM hbpmip/java-rapidminer:0.9.4

  MAINTAINER <your email>

  ENV DOCKER_IMAGE=hbpmip/my-algo:1.0.0 \
      JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/my-algo.jar \
      JAVA_ARGS=/eu/humanbrainproject/mip/algorithms/rapidminer/myalgo/settings.properties \
      MODEL=myalgo \
      FUNCTION=java-rapidminer-myalgo

  COPY --from=build-java-env /project/target/my-algo.jar /usr/share/jars/my-algo.jar
  COPY --from=build-java-env /project/target/site/ /var/www/html/
  COPY src/ /src/

  RUN chown -R compute:compute /src/ \
      && chown -R root:www-data /var/www/html/
```

# TODO
0) Parse the RapidMiner experiment template name from the algorithm field of the JSON input...
1) Better validation input data types (classification, using real values features for now!)
2) Add action section in PFA. Train the model using all the data as training set (separate RapidMiner process)
