# Metrics Cache

Stores precomputed statistics based on DQV values in a MongoDB.

## Table of Contents
1. [Build](#build)
1. [Run](#run)
1. [Docker](#docker)
1. [API](#api)
1. [Configuration](#configuration)
    1. [Environment](#environment)
    1. [Logging](#logging)
1. [License](#license)

## Build

Requirements:
 * Git
 * Maven 3
 * Java 11
 * MongoDB

```bash
$ git clone ...
$ mvn package
```
 
## Run

```bash
$ java -jar target/metrics-cache-fat.jar
```

## Docker

Build docker image:
```bash
$ docker build -t piveau/metrics-cache .
```

Run docker image:
```bash
$ docker run -it -p 8080:8080 piveau/metrics-cache
```

## API

A formal OpenAPI 3 specification can be found in the `src/main/resources/webroot/openapi.yaml` file.
A visually more appealing version is available at `{url}:{port}` once the application has been started.


## Configuration

### Environment

| Key | Description | Default |
| :--- | :--- | :--- |
| `PORT` | Port this service will run on | 8080 |
| `BASE_URI` | Base uri for the Graphs in the Triplestore | https://piveau.io/ |
| `CACHE_APIKEY` | **Mandatory**: The Apikey to refresh and clear the Cache.  |  |
| `CACHE_CORS_DOMAINS` | Domains from which CORS access should be allowed as Json Array. Example value: `["localhost","example.com"] ` |  |
| `MONGODB_SERVER_HOST` | Hostname this service will try to connect to for the mongo db | localhost |
| `MONGODB_SERVER_PORT` | Port this service will try to connect to for the mongo db | 27017 |
| `MONGODB_USERNAME` | Username this service will use for communicating with the Mongo DB | null |
| `MONGODB_PASSWORD` | Password this service will use for communicating with the Mongo DB |  |
| `MONGODB_DB_NAME` | Database name this service will use for communicating with the Mongo DB | metrics |
| `PIVEAU_TRIPLESTORE_CONFIG` | Triplestore configration. The value is a JSON Object, any field that is set in the JSON object for the value of this setting, wil overwrite the default, every field that is missing, will be ignored.  | ```{"address":"http://piveau-virtuoso:8890", "queryEndpoint": "/sparql", "queryAuthEndpoint":"/sparql-auth", "graphEndpoint":"/sparql-graph-crud", "graphAuthEndpoint":"/sparql-graph-crud-auth", "partitionSize":2500, "username":"dba", "password":"dba"}``` |
| `PIVEAU_IMPRINT_URL` |URL to imprint page. Used for OpenAPI GDPR complience|`/`|
| `PIVEAU_PRIVACY_URL` |URL to privacy policy page. Used for OpenAPI GDPR complience|`/`|

## License

[Apache License, Version 2.0](LICENSE.md)
