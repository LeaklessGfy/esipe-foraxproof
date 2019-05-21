#!/bin/sh
export JAVA_HOME=/usr/local/apps/alternatives/java/jdk-9+181
export PATH=${PATH}:$JAVA_HOME/bin
mvn clean package
