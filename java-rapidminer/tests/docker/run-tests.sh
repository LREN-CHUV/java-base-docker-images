#!/bin/sh

cp /usr/share/maven/ref/settings-docker.xml /root/.m2/settings.xml
mvn test
    
