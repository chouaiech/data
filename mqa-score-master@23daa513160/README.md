# Metrics Score
Calculates scores based on DQV measurements.

The service is based on the piveau-pipe-connector library. Any configuration applicable for the piveau-pipe-connector can also be used for this service.

## Table of Contents
1. [Build](#build)
1. [Run](#run)
1. [Docker](#docker)
1. [Configuration](#configuration)
    1. [Pipe](#pipe)
    1. [Data Info Object](#data-info-object)
    1. [Environment](#environment)
    1. [Logging](#logging)
1. [License](#license)

## Build

Requirements:
 * Git
 * Maven 3
 * Java 17

```bash
$ git clone <gitrepouri>
$ cd piveau-metrics-score
$ mvn package
```
 
## Run

```bash
$ java -jar target/metrics-score.jar
```

## Docker

Build docker image:
```bash
$ docker build -t piveau/piveau-metrics-score .
```

Run docker image:
```bash
$ docker run -it -p 8080:8080 piveau/piveau-metrics-score
```

## Configuration

### Pipe
The scoring does not count on any specific pipe configuration parameter.

### Environment
See also piveau-pipe-connector

| Variable| Description | Default Value |
| :--- | :--- | :--- |
| `PIVEAU_PLACEHOLDER` | Placeholder for the first environment variable | `default` |

### File
To override the default score values provide a turtle file named `config/piveau-dqv-vocabulary-score-values.ttl`.
It should contain values for all defined measurements, e.g.:

```
pv:formatAvailability
    pv:trueScore            "20"^^xsd:integer ;
    pv:falseScore            "-5"^^xsd:integer .
```

At least the `pv:trueScore` value should be defined. You can copy the `src/main/resources/piveau-dqv-vocabulary-default-score-values.ttl`
and adjust it for your need. 

### Logging
See [logback](https://logback.qos.ch/documentation.html) documentation for more details

| Variable                   | Description                                       | Default Value                         |
|:---------------------------|:--------------------------------------------------|:--------------------------------------|
| `PIVEAU_PIPE_LOG_APPENDER` | Configures the log appender for the pipe context  | `STDOUT`                              |
| `PIVEAU_LOGSTASH_HOST`     | The host of the logstash service                  | `logstash`                            |
| `PIVEAU_LOGSTASH_PORT`     | The port the logstash service is running          | `5044`                                |
| `PIVEAU_PIPE_LOG_PATH`     | Path to the file for the file appender            | `logs/piveau-pipe.%d{yyyy-MM-dd}.log` |
| `PIVEAU_PIPE_LOG_LEVEL`    | The log level for the pipe context                | `INFO`                                |
| `PIVEAU_LOG_LEVEL`         | The general log level for the `io.piveau` package | `INFO`                                |

## License

[Apache License, Version 2.0](LICENSE.md)
