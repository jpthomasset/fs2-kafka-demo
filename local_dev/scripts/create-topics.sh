#!/bin/bash

set -e

if [ $# -lt 2 ]; then
    echo "Usage: $0 <kafka-bootstrap-server> <topic1> <topic2> ..."
    exit 1
fi

bootstrap_server=$1

shift

for topic in $@; do
    # if topic name contains colon, then it is a partitioned topic
    if [[ $topic == *":"* ]]; then
        partitions=$(echo $topic | cut -d: -f2)
        topic=$(echo $topic | cut -d: -f1)
        kafka-topics --bootstrap-server $bootstrap_server --create --topic $topic --partitions $partitions --if-not-exists
    else
        kafka-topics --bootstrap-server $bootstrap_server --create --topic $topic --if-not-exists
    fi
done

