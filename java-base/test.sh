#!/bin/bash

if [[ $NO_SUDO || -n "$CIRCLECI" ]]; then
  DOCKER="docker"
elif groups $USER | grep &>/dev/null '\bdocker\b'; then
  DOCKER="docker"
else
  DOCKER="sudo docker"
fi

$DOCKER run -it --rm hbpmip/java-base java -version
