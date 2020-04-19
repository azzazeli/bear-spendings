#!/bin/sh

echo "Stopping application ..."
kill $(cat ./pid.file)
