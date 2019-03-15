#!/usr/bin/env bash
set -x
set -e 
cd harmonize \
    && sbt assembly \
    && cd ..

docker-compose build