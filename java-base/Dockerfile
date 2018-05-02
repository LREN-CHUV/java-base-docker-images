FROM openjdk:10.0.1-10-jre-slim-sid

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

ENV DOCKERIZE_VERSION=v0.6.1 \
    JAVA_OPTIONS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"

RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates wget \
    && update-ca-certificates \
    && wget -O /tmp/dockerize.tar.gz "https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-${DOCKERIZE_VERSION}.tar.gz" \
    && tar -C /usr/local/bin -xzvf /tmp/dockerize.tar.gz \
    && chown root:root /usr/local/bin/dockerize \
    && apt-get remove -y wget \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/* /tmp/*

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/java-base" \
      org.label-schema.description="Base Java runtime, using OpenJDK" \
      org.label-schema.url="https://github.com/LREN-CHUV/java-base-docker-images" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/java-base-docker-images.git" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="Apache2.0" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.schema-version="1.0"
