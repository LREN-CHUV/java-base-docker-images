[![DockerHub](https://img.shields.io/badge/docker-hbpmip%2Fchaos--algorithm-008bb8.svg)](https://hub.docker.com/r/hbpmip/chaos-algorithm/) [![ImageVersion](https://images.microbadger.com/badges/version/hbpmip/chaos-algorithm.svg)](https://hub.docker.com/r/hbpmip/chaos-algorithm/tags "hbpmip/chaos-algorithm image tags") [![ImageLayers](https://images.microbadger.com/badges/image/hbpmip/chaos-algorithm.svg)](https://microbadger.com/#/images/hbpmip/chaos-algorithm "hbpmip/chaos-algorithm on microbadger")

# hbpmip/chaos-algorithm: A faulty by design algorithm for testing

This algorithm deliberately fails and introduce defects and helps to design different failure scenario during testing.


## Usage

Run the Docker image using:


```sh

  docker run --rm -e PARAM_variables=v1 -e PARAM_covariables=v2,v3 -e MODEL_PARAM_failure=<failure mode> hbpmip/chaos-algorithm:0.1.1 compute

```

where failure mode is one of:

* training_fails: produces a failure during training. No results are returned - TODO: should return a PFA document emitting error
* never_stop: the processing never stops and this container keeps running indefinitely
* no_results: no results are returned and nothing is stored in the result database
* invalid_json: the PFA document returned is no a valid JSON document
* invalid_pfa_syntax: the PFA document returned is not valid according to PFA specification
* invalid_pfa_semantics: the PFA document returned contains invalid semantics (syntax error)
