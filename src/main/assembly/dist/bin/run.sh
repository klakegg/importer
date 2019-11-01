#!/bin/sh

DIR=$(dirname $(readlink -f $0))

java ${JAVA_OPTS:-} -classpath .:${DIR}/../lib/* net.klakegg.importer.Main $@