#!/bin/sh

echo "Starting application ..."
JAR=`ls spring-app-*.jar`
java -jar -Dspring.profiles.active=prod $JAR & echo $! > ./pid.file &
