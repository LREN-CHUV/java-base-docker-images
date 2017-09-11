FROM hbpmip/scala-base-build:latest

COPY build.sbt /build/
COPY project/ /build/project/
COPY src/ /build/src/

RUN sbt compile
