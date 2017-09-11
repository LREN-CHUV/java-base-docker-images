[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fjava--mip-008bb8.svg)](https://hub.docker.com/r/hbpmip/java-mip/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/java-mip.svg)](https://hub.docker.com/r/hbpmip/java-mip/tags "hbpmip/java-mip image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/java-mip.svg)](https://microbadger.com/#/images/hbpmip/java-mip "hbpmip/java-mip on microbadger")

# hbpmip/java-mip: Adapt the base Java image to the MIP environment

This image provides a Java environment compatible with MIP and with the following features:

* The *compute* user is used to run the Java programs
* Directories /data/in and /data/out are intended to store the incoming files
  and outgoing files for the computations. They can be mounted on an external filesystem.
* The environment variables COMPUTE_IN and COMPUTE_OUT can be used to locate those folders from the Java programs.
* In the /src directory you should place all scripts and libraries used to perform the computation.
* If you run the container with the *export* command and mount /data/out to a local directory,
  the source files will be copied to that local directory.
* If you run the container with the *serve* command, a web server will run and display any content located in /var/www/html/.
  You should place in this folder the documentation for the container.
* If you run the container with the *export-docs* command and mount /data/out to a local directory,
  the documentation will be copied to that local directory.
* If you run the container with the *shell* command, an interactive shell will start.

## Usage

Use this image as the parent image to adapt a Java-based algorithm to the MIP platform:

Dockerfile
```
  FROM hbpmip/java-base-build:3.5.0-jdk-8-7 as build-java-env

  COPY pom.xml /project/pom.xml
  COPY src/ /project/src

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn package assembly:single site

  FROM hbpmip/java-mip:0.2.1

  MAINTAINER <your email>

  ENV JAVA_CLASSPATH=/usr/share/jars/my-algo.jar
  ENV JAVA_MAINCLASS=org.myorg.myalgo.Main

  COPY --from=build-java-env /project/target/my-algo-jar-with-dependencies.jar /usr/share/jars/my-algo.jar
  COPY --from=build-java-env /project/target/site/ /var/www/html/
  COPY src/ /src/

  RUN chown -R compute:compute /src/ \
      && chown -R root:www-data /var/www/html/
```

## See also

You may want to use one of the following specialised images for your algorithm instead:

* [hbpmip/java-rapidminer](../java-rapidminer/README.md): for algorithms based on RapidMiner

## Summary of commands:

* Run the main computations
  ````
    mkdir doc && docker run --rm -v $(pwd)/in:/data/in -v $(pwd)/out:/data/out <image name> compute
  ````
* Export the documentation to the ./doc directory
  ````
    docker run --rm -v /data/out:./doc <image name> export-docs
  ````
* Interactive shell
  ````
    docker run -i -t --rm <image name> shell
  ````
* Quick documentation accessible at http://localhost:7777/ and sources at http://localhost:7777/src/
  Stop the server using Ctrl+C from the command line.
  ````
    docker run -d --rm -p 7777:80 <image name> serve
  ````
* Export the sources to the ./src directory
  ````
    mkdir src && docker run --rm -v $(pwd)/src:/data/out <image name> export
  ````
* Export the documentation to the ./doc directory
  ````
    mkdir doc && docker run --rm -v $(pwd)/doc:/data/out <image name> export-docs
  ````

## Useful environment variables:

* COMPUTE_IN: the directory containing the input files
* COMPUTE_OUT: the output directory to use to store the output files
* COMPUTE_TMP: the directory to use to store temporary files
* SRC: the directory containing the sources
* JAR_PATH: the path to the executable jar
