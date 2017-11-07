# Pull base image
FROM hbpmip/java-base-build:3.5.0-jdk-8-9

#
# Scala and sbt Dockerfile
# Originally taken from
# https://github.com/hseeberger/scala-sbt
#

MAINTAINER Arnaud Jutzeler <arnaud.jutzeler@chuv.ch>

ENV SCALA_VERSION=2.11.8 \
    SBT_VERSION=0.13.16 \
    HOME=/root

# Install sbt dependencies
RUN apk add --no-cache ncurses git

# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.tgz https://cocl.us/sbt-$SBT_VERSION.tgz && \
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

RUN cd /seed && sbt about && sbt sbtVersion compile && rm -rf /seed/target && mv /root/.ivy2/cache/* /usr/share/ivy/ref/repository/

# Define working directory
WORKDIR /build
