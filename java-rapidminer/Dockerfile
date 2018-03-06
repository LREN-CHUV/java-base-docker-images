FROM hbpmip/java-base-build:3.5.2-jdk-8-0 as build-java-env

COPY pom.xml /project/pom.xml
# Fill the cache for jar dependencies, to allow code changes reusing previously downloaded jars
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn dependency:go-offline

COPY src/ /project/src

# Repeating copy of the settings works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn package site

FROM hbpmip/java-mip:0.5.7

MAINTAINER ludovic.claude@chuv.ch

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

ENV JAVA_CLASSPATH=${JAVA_CLASSPATH}:/usr/share/jars/mip-rapidminer-deps.jar \
    JAVA_MAINCLASS=eu.humanbrainproject.mip.algorithms.rapidminer.Main

COPY --from=build-java-env /usr/share/maven/ref/repository/org/postgresql/postgresql/9.4.1212.jre7/postgresql-9.4.1212.jre7.jar /usr/share/jars/postgres-9.4.jar
COPY --from=build-java-env /project/target/rapidminer.jar /usr/share/jars/mip-rapidminer.jar
COPY --from=build-java-env /project/target/rapidminer-deps.jar /usr/share/jars/mip-rapidminer-deps.jar
COPY --from=build-java-env /project/target/site/ /mip/java-rapidminer/docs/
COPY src/ /mip/java-rapidminer/src/

RUN chown -R compute:compute /mip/

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/java-rapidminer" \
      org.label-schema.description="Execute RapidMiner algorithms in MIP Woken environment" \
      org.label-schema.url="https://github.com/LREN-CHUV/java-rapidminer" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/java-rapidminer.git" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="AGPLv3" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.schema-version="1.0"
