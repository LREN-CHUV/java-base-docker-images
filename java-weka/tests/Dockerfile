FROM hbpmip/java-base-build:3.5.0-jdk-8-8 as build-java-env

COPY pom.xml /project/pom.xml
COPY src/main/ /project/src/main/

# Repeating the file copy works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn package site

FROM hbpmip/java-weka:latest

ENV JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/java-weka-test.jar
ENV JAVA_ARGS=/eu/humanbrainproject/mip/algorithms/weka/simplelr/settings.properties
ENV MODEL simplelr
ENV FUNCTION java-weka-simplelr

COPY --from=build-java-env /project/target/java-weka-test.jar /usr/share/jars/java-weka-test.jar
COPY --from=build-java-env /project/target/site/ /var/www/html/
COPY src/ /src/

RUN chown -R compute:compute /src/ \
    && chown -R root:www-data /var/www/html/