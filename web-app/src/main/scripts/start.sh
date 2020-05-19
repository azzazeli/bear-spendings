#!/bin/sh

echo "Starting application ..."
JAR=`ls bear-spendings-*.jar`
java -Dspring.profiles.active=prod -Dspring.security.user.name=a -Dspring.security.user.password=b -jar $JAR & echo $! > ./pid.file &
