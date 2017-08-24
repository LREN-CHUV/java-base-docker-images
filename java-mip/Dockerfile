FROM hbpmip/java-base-build:3.5.0-jdk-8-1 as build-java-env

COPY pom.xml /project/pom.xml
COPY src/ /project/src

# Repeating the file copy works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn assembly:single

FROM hbpmip/java-base:8u131-1

MAINTAINER arnaud@ahead-solutions.ch

LABEL eu.humanbrainproject.version=0.2 \
      eu.humanbrainproject.category=Java

COPY scripts/setup.sh /usr/local/bin/java-mip-docker-setup

ENV COMPUTE_IN=/data/in \
    COMPUTE_OUT=/data/out \
    COMPUTE_TMP=/tmp \
    ERROR_FILE=/data/out/errors.txt \
    OUTPUT_FILE=/data/out/output.txt \
    SRC=/src

RUN /usr/local/bin/java-mip-docker-setup

VOLUME /data/in
VOLUME /data/out

COPY --from=build-java-env /project/target/java-mip-jar-with-dependencies.jar /usr/share/jars/

COPY scripts/docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 80
CMD ["compute"]

LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/java-mip" \
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
