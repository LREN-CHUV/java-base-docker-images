
# hbpmip/java-base-buildbase: Java build environment, including Maven

This image contains the Java JDK and Maven 3 (from [Maven image](https://hub.docker.com/r/_/maven/))

## Usage

Dockerfile
```
  FROM hbpmip/java-build-base:3.5.0-jdk-8-0

  COPY pom.xml /project/
  COPY src/ /project/src/

  # Repeating the file copy works better. I dunno why.
  RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
      && mvn clean package

```
