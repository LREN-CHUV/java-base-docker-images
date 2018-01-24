FROM hbpmip/java-base-build:3.5.2-jdk-8-0 as build-java-env

COPY pom.xml /project/pom.xml
# Fill the cache for jar dependencies, to allow code changes reusing previously downloaded jars
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn dependency:resolve

COPY src/ /project/src

# Repeating copy of the settings works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn -Dmaven.test.skip=true package site

FROM hbpmip/java-weka:latest

ENV DOCKER_IMAGE=java-weka-tests \
    JAVA_CLASSPATH=${JAVA_CLASSPATH}:/opt/weka/props/:/usr/share/jars/java-weka-test.jar \
    JAVA_ARGS=/eu/humanbrainproject/mip/algorithms/weka/simplelr/settings.properties \
    MODEL=simplelr \
    FUNCTION=java-weka-simplelr

COPY --from=build-java-env /project/target/java-weka-test.jar /usr/share/jars/java-weka-test.jar
COPY --from=build-java-env /project/target/site/ /var/www/html/
COPY src/ /src/

RUN chown -R compute:compute /src/ \
    && chown -R root:www-data /var/www/html/
