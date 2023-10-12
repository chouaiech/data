# SHACL Validator

This service allows to validate RDF files against SHACL files. It is shipped with SHACL validation for DCAT-AP and written in Kotlin.

The service is based on the piveau-pipe-connector library, but it can also be used as a standalone-service. Any configuration applicable for the pipe-connector can also be used for this service.

## Table of Contents
1. [Build](#build)
2. [Run](#run)
3. [Docker](#docker)
4. [Usage](#usage)
   1. [Using the API](#using-the-api)
   2. [Adding additional shapes](#adding-additional-shapes) 
5. [Configuration](#configuration)
   1. [Pipe](#pipe)
   2. [Environment](#environment)
   3. [Logging](#logging)
6. [License](#license)

## Build

Requirements:
 * Git
 * Maven 3
 * JDK 17

```bash
$ git clone <gitrepouri>
$ cd piveau-metrics-validating-shacl
$ mvn package
```
 
## Run

```bash
$ java -jar target/validating-shacl.jar
```

## Docker

Build docker image:
```bash
$ docker build -t piveau/metrics-validating-shacl .
```

Run docker image:
```bash
$ docker run -it -p 8080:8080 piveau/metrics-validating-shacl
```

## Usage

### Using the API

The services exposes two kinds of endpoints. On the base path (e.g. `http://localhost:8080/`) the pipe endpoint is available. It is mostly meant to be used in a [piveau pipeline](https://doc.piveau.eu/consus/).
On the sub-path `/shacl` (e.g. `http://localhost:8080/shacl`) an API for direct SHACL validation is available. It can be used to directly validate a file against a SHACL shape. For example: 

```
$ curl -X POST "http://localhost:8080/shacl/validation/report" -H "Content-Type: text/turtle" -d "@src/test/resources/test.ttl"
```

### Adding additional shapes

You can easily add additional shapes programmatically. 

- Add your shapes in a new directory to the `resources/rdf/shapes` directory
- Add a shape object in `kotlin/io/piveau/validating/DCATAPShapes.kt` and add a fitting key to the `findShapes` function
- Add the key to the OpenAPI specification in `resources/webroot/index-shacl.html`

## Configuration

### Pipe
* `skip`: Skip validation
* `shapeModel`: The DCAT-AP shacl shape version to use. Possible values are
   * `shapes211level1` (default)
   * `shapes211level2`
   * `shapes211level3`
   * `shapes211`
   * `shapes210level1`
   * `shapes210`
   * `shapes201`
   * `shapes200`
   * `shapes121`
   * `shapes121orig`
   * `shapes12`
   * `shapes11`
   * `shapes11orig`
     
### Environment
See also piveau-pipe-connector

| Variable                          | Description                                                                                                                              | Default Value |
|:----------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------|:--------------|
| `PIVEAU_SHACL_VERTICLE_INSTANCES` | Configure the number of verticle instances. This should be maximum the number of cores                                                   | `1`           |
| `PIVEAU_SHACL_WORKER_POOL_SIZE`   | Configure the worker pool size for the shacl verticles. It seems to be less memory consuming when equally set to the number of instances | `20`          |
| `PIVEAU_SHACL_SHAPES_CONFIG`      | Configure shacl shapes.                                                                                                                  | `{}`          |
| `PIVEAU_PIPE_ENDPOINT_PORT`       | Sets the port of the service.                                                                                                            | `8080`        |
| `PIVEAU_IMPRINT_URL`              | URL to imprint page. Used for OpenAPI GDPR complience                                                                                    | `/shacl`      |
| `PIVEAU_PRIVACY_URL`              | URL to privacy policy page. Used for OpenAPI GDPR complience                                                                             | `/shacl`      |

### Logging
See [logback](https://logback.qos.ch/documentation.html) documentation for more details

| Variable                   | Description                                                                                                | Default Value                         |
|:---------------------------|:-----------------------------------------------------------------------------------------------------------|:--------------------------------------|
| `PIVEAU_PIPE_LOG_APPENDER` | Configures the log appender for the pipe context. Possible values are `STDOUT`, `LOGSTASH`, and `PIPEFILE` | `STDOUT`                              |
| `PIVEAU_LOGSTASH_HOST`     | The host of the logstash service in case of `LOGSTASH`appender                                             | `logstash`                            |
| `PIVEAU_LOGSTASH_PORT`     | The port the logstash service is running                                                                   | `5044`                                |
| `PIVEAU_PIPE_LOG_PATH`     | Path to the file for the file appender                                                                     | `logs/piveau-pipe.%d{yyyy-MM-dd}.log` |
| `PIVEAU_PIPE_LOG_LEVEL`    | The log level for the pipe context                                                                         | `INFO`                                |
| `PIVEAU_LOG_LEVEL`         | The general log level for the `io.piveau` package                                                          | `INFO`                                |

## License

[Apache License, Version 2.0](LICENSE.md)
