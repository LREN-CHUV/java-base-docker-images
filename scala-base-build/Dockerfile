# Pull base image
FROM hbpmip/java-base-build:3.6.0-jdk-11-0

#
# Scala and sbt Dockerfile
# Originally taken from
# https://github.com/hseeberger/scala-sbt
#

ENV SCALA_VERSION=2.12.8 \
    SBT_VERSION=1.2.8 \
    HOME=/root

# Install sbt dependencies
RUN apt-get update && apt-get install -y ncurses-bin git \
    && rm -rf /var/lib/apt/lists/*

# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.tgz https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz && \
  mkdir -p /opt /usr/share/sbt/ref /usr/share/ivy/ref/repository && \
  tar -x -v -z -C /opt -f sbt-$SBT_VERSION.tgz && \
  rm sbt-$SBT_VERSION.tgz && \
  ln -s /opt/sbt/bin/sbt /usr/local/bin

# Install Scala
## Piping curl directly in tar
RUN \
  curl -fsL http://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /opt/ && \
  echo >> /root/.bashrc && \
  echo 'export PATH=~/scala-$SCALA_VERSION/bin:$PATH' >> /root/.bashrc

WORKDIR /seed

COPY docker/build.sbt /seed/
COPY docker/project/ /seed/project/
COPY docker/src/ /seed/src/

RUN cd /seed && sbt about && sbt sbtVersion +compile && rm -rf /seed/target && mv /root/.ivy2/cache/* /usr/share/ivy/ref/repository/

COPY docker/warm-ivy2-cache.sh docker/check-sources.sh /

# Define working directory
ONBUILD WORKDIR /build
# Warm Ivy2 cache
ONBUILD RUN /warm-ivy2-cache.sh

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/scala-base-build" \
      org.label-schema.description="Base image for building Scala projects with sbt" \
      org.label-schema.url="https://github.com/LREN-CHUV/scala-base-build" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/scala-base-build.git" \
      org.label-schema.vcs-ref="$VCS_REF" \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="Apache2.0" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.schema-version="1.0"
