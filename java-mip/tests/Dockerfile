FROM hbpmip/java-base-build:3.5.0-jdk-8-6 as build-java-env

COPY pom.xml /project/pom.xml
COPY src/ /project/src

# Repeating the file copy works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn package assembly:single site

FROM hbpmip/java-mip:latest

MAINTAINER arnaud@ahead-solutions.ch

ENV JAVA_CLASSPATH=/usr/share/jars/java-mip.jar
ENV JAVA_MAINCLASS=eu.hbp.mip.container.Main

COPY --from=build-java-env /project/target/java-mip-jar-with-dependencies.jar /usr/share/jars/java-mip.jar
COPY --from=build-java-env /project/target/site/ /var/www/html/
COPY src/ /src/

RUN chown -R compute:compute /src/ \
    && chown -R root:www-data /var/www/html/