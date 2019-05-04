#!/usr/bin/env bash

docker rm -f -v mf1 mf2 mf3

docker run -d -p 8080:8080 --name mf1 mapfinder
docker run -d -p 8081:8080 --name mf2 mapfinder
docker run -d -p 8082:8080 --name mf3 mapfinder