# Pull base image
FROM maven:3.6.0-jdk-11-slim

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

RUN mkdir -p /root/.m2
ENV HOME=/root
COPY docker/seed-src /seed-src
WORKDIR /seed-src

# Run Maven on an empty project and force it to download most of its dependencies to fill the cache
RUN mkdir -p /usr/share/maven/ref/repository \
    && cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mkdir -p /project \
    && mvn clean \
        resources:resources \
        compiler:compile \
        surefire:test \
        jar:jar \
        package \
        assembly:single \
        site \
        dependency:go-offline -B \
    && rm -rf /seed-src/target

WORKDIR /project

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/java-base-build" \
      org.label-schema.description="Java build environment, including Maven" \
      org.label-schema.url="https://github.com/LREN-CHUV/java-base-docker-images" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/java-base-docker-images.git" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="Apache2.0" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.schema-version="1.0"
