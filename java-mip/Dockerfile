FROM hbpmip/java-base:8u131-1

MAINTAINER arnaud@ahead-solutions.ch

LABEL eu.humanbrainproject.version=0.2 \
      eu.humanbrainproject.category=Java

COPY docker/setup.sh /usr/local/bin/java-mip-docker-setup
COPY docker/etc/nginx/ /etc/nginx/

ENV COMPUTE_IN=/data/in \
    COMPUTE_OUT=/data/out \
    COMPUTE_TMP=/tmp \
    ERROR_FILE=/data/out/errors.txt \
    OUTPUT_FILE=/data/out/output.txt \
    RESULT_TABLE=job_result \
    SRC=/src

RUN /usr/local/bin/java-mip-docker-setup

VOLUME /data/in
VOLUME /data/out

COPY docker/docker-entrypoint.sh /

ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 80
CMD ["compute"]

LABEL org.label-schema.build-date=$BUILD_DATE \
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
