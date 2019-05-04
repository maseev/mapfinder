Map Finder
==========
[![Build Status](https://travis-ci.org/maseev/mapfinder.svg?branch=master)](https://travis-ci.org/maseev/mapfinder)
[![Coverage Status](https://coveralls.io/repos/github/maseev/mapfinder/badge.svg?branch=master)](https://coveralls.io/github/maseev/mapfinder?branch=master)

The [Point in polygon](https://en.wikipedia.org/wiki/Point_in_polygon) problem in a distributed environment
-----------

Given a JSON file which represents an array of geographic maps:

```json
[
  {
    "id": 500,
    "polygon": [
      {
        "lat": 40.3751945,
        "lng": 49.8531609
      },
      {
        "lat": 41.3008919,
        "lng": 69.2825928
      },
      ...
    ]
  },
  ...
]
```
Every map is an object which consists of two fields - a map id and an array of coordinates.

We need to develop a function which would accept a point (described by latitude and longitude) 
as an input and return an array of maps IDs which include this point.

It's assumed there are too many maps to process by one application instance.
In order to speed up the computation, we need to develop a distributed application which would 
partition the incoming data and provide a way to run the point inclusion in polygon algorithm on 
cluster nodes in a distributed manner.


Application architecture
---------------
The application uses the 
[PNPOLY](https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html) algorithm by W. Randolph 
Franklin to test a point inclusion in a polygon and [Hazelcast](https://hazelcast.org) for 
data partitioning and distributed computing. It also uses [Spring Boot](https://spring.io/projects/spring-boot) to provide the REST API 
which clients can use to upload configuration JSON files and search for maps.

How to build
------------
* Clone this repository
* Run ``` mvn package ``` inside the project folder in order to build the project artifact
* Build a Docker image by ``` docker build -t mapfinder .  ```

How to run
----------
* Use the `start.sh` script to start a cluster of three Docker containers.
* Run the `stop.sh` script to stop the cluster.

How to use
----------
After you start the cluster with the `start.sh` script, the applications REST API will be 
available on the `8080`, `8081` and `8082` ports respectively. 

#### Configuration upload
You can upload a JSON file with geographic maps with the following command:
 
`curl -F "data=@maps.json" http://localhost:8080/api/map`

>Notice, that you have the `maps.json` file with some test data inside the project folder

#### Maps searching
Use the following command to find all maps IDs that include the point which is described by 
latitude and longitude:

`curl 'http://localhost:8080/api/map?latitude=0.5&longitude=0.5'`