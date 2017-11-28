FROM hbpmip/java-base-build:3.5.0-jdk-8-9 as build-java-env

COPY pom.xml /project/pom.xml
# Fill the cache for jar dependencies, to allow code changes reusing previously downloaded jars
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn dependency:go-offline

COPY src/ /project/src

# Repeating copy of the settings works better. I dunno why.
RUN cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml \
    && mvn package site

FROM hbpmip/java-base:8u131-2

MAINTAINER Arnaud Jutzeler <arnaud.jutzeler@chuv.ch>

ARG BUILD_DATE
ARG VCS_REF
ARG VERSION

COPY docker/setup.sh /usr/local/bin/java-mip-docker-setup

ENV COMPUTE_IN=/data/in \
    COMPUTE_OUT=/data/out \
    COMPUTE_TMP=/tmp \
    ERROR_FILE=/data/out/errors.txt \
    OUTPUT_FILE=/data/out/output.txt \
    RESULT_TABLE=job_result \
    SRC=/src \
    JAVA_CLASSPATH=/usr/share/jars/postgres-9.4.jar:/usr/share/jars/mip-adapter-deps.jar

RUN /usr/local/bin/java-mip-docker-setup
COPY docker/etc/nginx/ /etc/nginx/
RUN /usr/sbin/nginx -t

COPY --from=build-java-env /usr/share/maven/ref/repository/org/postgresql/postgresql/9.4.1212.jre7/postgresql-9.4.1212.jre7.jar /usr/share/jars/postgres-9.4.jar
COPY --from=build-java-env /project/target/adapter.jar /usr/share/jars/mip-adapter.jar
COPY --from=build-java-env /project/target/adapter-deps.jar /usr/share/jars/mip-adapter-deps.jar
COPY --from=build-java-env /project/target/site/ /mip/java-adapter/docs/

VOLUME /data/in
VOLUME /data/out

COPY docker/docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 80
CMD ["compute"]

LABEL eu.humanbrainproject.version=0.5 \
      eu.humanbrainproject.category=Java \
      org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="hbpmip/java-mip" \
      org.label-schema.description="Java environment compatible with MIP" \
      org.label-schema.url="https://github.com/LREN-CHUV/java-base-docker-images" \
      org.label-schema.vcs-type="git" \
      org.label-schema.vcs-url="https://github.com/LREN-CHUV/java-base-docker-images.git" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.version="$VERSION" \
      org.label-schema.vendor="LREN CHUV" \
      org.label-schema.license="Apache2.0" \
      org.label-schema.docker.dockerfile="Dockerfile" \
      org.label-schema.schema-version="1.0"
