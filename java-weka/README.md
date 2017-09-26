[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--weka-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-weka/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-weka.svg)](https://hub.docker.com/r/hbpmip/java-weka/tags "hbpmip/java-weka image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-weka.svg)](https://microbadger.com/#/images/hbpmip/java-weka "hbpmip/java-weka on microbadger")

# hbpmip/java-weka: Run RapidMinder algorithms in MIP

Run Rapidminer algorithms (experiments) in MIP platform.

For the moment only classification algorithms...

It can run any RapidMiner experiments (based on classifier) as long as the .rmp template is present
in the data/in/template volume folder

## Usage

Use this image as the parent image to adapt a RapidMiner algorithm to the MIP platform:

Dockerfile
```
  FROM hbpmip/java-base-build:3.5.0-jdk-8-7 as build-java-env

  COPY pom.xml /project/pom.xml
  COPY src/ /project/src

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn package site

  FROM hbpmip/java-weka:0.8.0

  MAINTAINER <your email>

  ENV JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/my-algo.jar
  ENV JAVA_MAINCLASS=org.myorg.myalgo.Main

  COPY --from=build-java-env /project/target/my-algo-jar-with-dependencies.jar /usr/share/jars/my-algo.jar
  COPY --from=build-java-env /project/target/site/ /var/www/html/
  COPY src/ /src/

  RUN chown -R compute:compute /src/ \
      && chown -R root:www-data /var/www/html/
```

# TODO
0) Parse the RapidMiner experiment template name from the algorithm field of the JSON input...
1) Better validation input data types (classification, using real values features for now!)
2) Add action section in PFA. Train the model using all the data as training set (separate RapidMiner process)
