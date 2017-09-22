FROM hbpmip/java-base-build:3.5.0-jdk-8-7 as build-java-env

COPY pom.xml /project/pom.xml
COPY src/main/ /project/src/main/

# Repeating the file copy works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn package site

FROM hbpmip/java-rapidminer:latest

ENV JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/java-rapidminer-test.jar
ENV JAVA_ARGS=/eu/humanbrainproject/mip/algorithms/rapidminer/tests/models/settings.properties
ENV MODEL rpm
ENV FUNCTION java-rapidminer-rpm

COPY --from=build-java-env /project/target/java-rapidminer-test.jar /usr/share/jars/java-rapidminer-test.jar
COPY --from=build-java-env /project/target/site/ /var/www/html/
COPY src/ /src/

RUN chown -R compute:compute /src/ \
    && chown -R root:www-data /var/www/html/
