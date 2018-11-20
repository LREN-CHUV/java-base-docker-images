FROM hbpmip/java-base-build:3.6.0-jdk-11-0 as build-java-env

COPY pom.xml /project/pom.xml
# Fill the cache for jar dependencies, to allow code changes reusing previously downloaded jars
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn dependency:resolve

COPY src/main/ /project/src/main/

# Repeating copy of the settings works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn package site

FROM hbpmip/java-rapidminer:latest

ENV DOCKER_IMAGE=java-rapidminer-tests \
    JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/java-rapidminer-test.jar \
    JAVA_ARGS=/eu/humanbrainproject/mip/algorithms/rapidminer/rpmdefault/settings.properties \
    MODEL=rpm \
    FUNCTION=java-rapidminer-rpm

COPY --from=build-java-env /project/target/java-rapidminer-test.jar /usr/share/jars/java-rapidminer-test.jar
COPY --from=build-java-env /project/target/site/ /var/www/html/
COPY src/ /src/

RUN chown -R compute:compute /src/ \
    && chown -R root:www-data /var/www/html/
