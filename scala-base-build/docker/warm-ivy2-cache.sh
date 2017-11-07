#!/bin/bash

echo "Warming Ivy cache..."
mkdir -p /root/.ivy2/
rm -rf /root/.ivy2/cache/
ln -s /usr/share/ivy/ref/repository /root/.ivy2/cache
