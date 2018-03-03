FROM hbpmip/java-base-build:3.5.0-jdk-8-9 as build-java-env

COPY pom.xml /project/pom.xml
COPY src/ /project/src

# Fill the cache for jar dependencies, to allow code changes reusing previously downloaded jars
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn dependency:go-offline

# Repeating copy of the settings works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \ 
    && mvn package site

FROM hbpmip/java-mip:0.5.6

MAINTAINER martin.breskvar@ijs.si

ENV JAVA_CLASSPATH=${JAVA_CLASSPATH}:/opt/weka/props/:/usr/share/jars/mip-jsi-clus-deps.jar \
    JAVA_MAINCLASS=eu.humanbrainproject.mip.algorithms.jsi.Main \
    WEKA_HOME=/opt/weka

COPY docker/databases-props/ /opt/weka/databases-props/
COPY --from=build-java-env /project/target/jsi-clus.jar /usr/share/jars/mip-jsi-clus.jar
COPY --from=build-java-env /project/target/jsi-clus-deps.jar /usr/share/jars/mip-jsi-clus-deps.jar
COPY --from=build-java-env /project/target/site/ /mip/java-jsi-clus/docs/
COPY src/ /mip/java-jsi-clus/src/

RUN mkdir -p /opt/weka/props/weka/experiment \
    && chown -R compute:compute /mip/ /opt/weka/ /usr/share/jars/


WORKDIR /usr/share/jars

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/java-jsi-clus" \
      org.label-schema.description="Execute CLUS (JSI) algorithms in MIP Woken environment" \
      org.label-schema.url="https://github.com/LREN-CHUV/java-jsi-clus" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/java-jsi-clus.git" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="JSI KT" \
      org.label-schema.license="GPLv3" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.schema-version="1.0"
