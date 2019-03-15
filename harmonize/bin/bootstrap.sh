#!/usr/bin/env bash


. /etc/confluent/docker/bash-config

dub ensure BOOTSTRAP_SERVERS
dub ensure SCHEMA_REGISTRY_URL

echo "===> Check if Kafka is healthy ..."

cub kafka-ready \
    "${CONNECT_CUB_KAFKA_MIN_BROKERS:-1}" \
    "${CONNECT_CUB_KAFKA_TIMEOUT:-40}" \
    -b "$BOOTSTRAP_SERVERS"

java -jar /root/harmonize.jar $BOOTSTRAP_SERVERS $SCHEMA_REGISTRY_URL