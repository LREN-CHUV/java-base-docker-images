#!/usr/bin/env bash

cd /build

count=$(git status --porcelain | wc -l)
if test $count -gt 0; then
    git status
    echo "Not all files have been copied to Docker. Build aborted"
    exit 1
fi
