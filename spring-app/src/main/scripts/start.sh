#!/bin/sh

echo "Starting application ..."
JAR=`ls bear-spendings-*.jar`
java -jar -Dspring.profiles.active=prod $JAR & echo $! > ./pid.file &
