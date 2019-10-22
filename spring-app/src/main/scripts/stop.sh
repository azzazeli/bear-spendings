#!/bin/sh

echo "Stoping application ..."
kill $(cat ./pid.file)
