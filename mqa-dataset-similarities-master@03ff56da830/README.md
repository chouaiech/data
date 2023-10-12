# Dataset Similarity Service

Fingerprint datasets for later comparison and provides and endpoint to retrieve similar datasets for a given ID using a distance metric.

## Table of Contents
1. [Build](#build)
1. [Run](#run)
1. [Docker](#docker)
1. [Configuration](#configuration)
    1. [Data Info Object](#data-info-object)
    1. [Environment](#environment)
    1. [Logging](#logging)
1. [License](#license)


## Build

Requirements:
 * Git
 * Maven 3
 * Java 11

```bash
$ git clone ...
$ mvn package
```

## Run

```bash
$ java -jar target/dataset-similarities.jar
```

## Docker

Build docker image:
```bash
$ docker build -t piveau/metrics-dataset-similarities .
```

Run docker image:
```bash
$ docker run -it -p 8080:8080 piveau/metrics-dataset-similarities
```

## API

A formal OpenAPI 3 specification can be found in the `src/main/resources/webroot/openapi.yaml` file.
A visually more appealing version is available at `{url}:{port}` once the application has been started.

## Configuration

### Environment

| Key | Description | Default |
| :--- | :--- | :--- |
| `PORT` | Port this service will run on | 8080 |
| `SPARQL_URL` | Address of the SPARQL endpoint | https://www.europeandataportal.eu/sparql |
| `DEFAULT_RESULT_SIZE` | Number of similarities to return if no `limit` parameter is provided | 10 |
| `SCHEDULES`                | Schedules for report generation                   | `[ ]`                                                                          |
| `CORS_DOMAINS`             | List of CORS domains                              | -                                                                              |


### Schedules
Schedules can be specified using a JSON Array containing a dedicated JSON object for each set of countries and schedule.
The schedule itself must be specified using [Cron](https://en.wikipedia.org/wiki/Cron) expressions.
Countries must be specified using their [ISO 3166-1 alpha-3](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) country codes.
The value `EUROPE` is available as an additional "country" code to cover EU catalogues.
An example is shown below:

```json
[
  {
    "countries": ["DEU", "GBR"],
    "cron": " 0 50 16 1/1 * ? * "
  },
  {
    "languages": ["FRA", "ITA"],
    "cron": " 0 55 16 1/1 * ? * "
  }
]
```

### Logging
See [logback](https://logback.qos.ch/documentation.html) documentation for more details

| Variable| Description | Default Value |
| :--- | :--- | :--- |
| `PIVEAU_PIPE_LOG_APPENDER` | Configures the log appender for the pipe context | `STDOUT` |
| `PIVEAU_LOGSTASH_HOST`            | The host of the logstash service | `logstash` |
| `PIVEAU_LOGSTASH_PORT`            | The port the logstash service is running | `5044` |
| `PIVEAU_PIPE_LOG_PATH`     | Path to the file for the file appender | `logs/piveau-pipe.%d{yyyy-MM-dd}.log` |
| `PIVEAU_PIPE_LOG_LEVEL`    | The log level for the pipe context | `INFO` |
| `PIVEAU_LOG_LEVEL`    | The general log level for the `io.piveau` package | `INFO` |

## License

[Apache License, Version 2.0](LICENSE.md)
