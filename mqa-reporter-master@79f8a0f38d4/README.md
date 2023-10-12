# Metrics Reporter

Provides a .pdf, .ods and .xlsx representation of the metrics measurements. In order to create the .pdf version of the
report, a Quickchart Docker image needs to be running. Please see below for further information.

## Table of Contents

- [Build](#build)
- [Run](#run)
- [Docker](#docker)
- [Quickchart](#quickchart)
- [Configuration](#configuration)
    - [Schedules](#schedules)
    - [Logging](#logging)
- [License](#license)

## Build

Requirements:

* Git
* Maven 3
* Java 11

```bash
$ git clone <gitrepouri>
$ cd metrics-reporter
$ mvn package
```

## Run

```bash
$ java -jar target/metrics-reporter-fat.jar
```

## Docker

Build docker image:

```bash
$ docker build -t piveau/metrics-reporter .
```

Run docker image:

```bash
$ docker run -it -p 8080:8080 piveau/metrics-reporter
```

## Quickchart

The Quickchart service is required for PDF generation. It can be disabled by omitting the variable `QUICKCHART_ADDRESS`.
To set up Quickchart, pull the Quickchart image from Docker

```bash
$ docker pull ianw/quickchart
```

Build and run it:

```bash
$ docker build -t ianw/quickchart .
$ docker run -p 8085:3400 ianw/quickchart
```

## Configuration

| Variable                             | Description                                                                          | Default Value                            |
|:-------------------------------------| :----------------------------------------------------------------------------------- |:-----------------------------------------|
| `PORT`                               | Sets the environment application port                                                | `8080`                                   |
| `API_KEY`                            | Secret for authorization.                                                            | -                                        |
| `QUICKCHART_ADDRESS`                 | The address of the Quickchart service. If not set the PDF generation is skipped.     | -                                        |
| `METRICS_CATALOGUES_ADDRESS`         | The address of the Metrics API                                                       | `http://piveau-metrics-cache:8080/`      |
| `SCHEDULES`                          | Schedules for report generation                                                      | `[ ]`                                    |
| `CORS_DOMAINS`                       | List of CORS domains                                                                 | -                                        |
| `PIVEAU_IMPRINT_URL`                 | URL to imprint page. Used for OpenAPI GDPR compliance                                | `/`                                      |
| `PIVEAU_PRIVACY_URL`                 | URL to privacy policy page. Used for OpenAPI GDPR compliance                         | `/`                                      |
| `FONT_REGULAR`                       | Name of regular font for PDF report, must be placed in `src/main/resources/fonts`    | `Roboto-Regular.ttf`                     |
| `FONT_BOLD`                          | Name of bold font for PDF report, must be placed in `src/main/resources/fonts`       | `Roboto-Bold.ttf`                        |
| `FONT_ITALIC`                        | Name of italic font for PDF report, must be placed in `src/main/resources/fonts`     | `Roboto-Italic.ttf`                      |
| `FONT_BOLD_ITALIC`                   | Name of bold italic font for PDF report, must be placed in `src/main/resources/fonts`| `Roboto-BoldItalic.ttf`                  |
| `HEADER_IMAGE`                       | Name of header image file for PDF report, must be placed in `src/main/resources`     | `header_image.png`                       |
| `SHACL_URL`                          | Link to SHACL service for use in the PDF report                                      | `https://data.europa.eu/shacl/`          |
| `METHODOLOGY_URL`                    | Link to metrics methodology page for use in the PDF report                           | `https://data.europa.eu/mqa/methodology` |

### Schedules

Schedules can be specified using a JSON Array containing a dedicated JSON object for each set of languages and schedule.
The schedule itself must be specified using [Cron](https://en.wikipedia.org/wiki/Cron) expressions. An example is shown
below:

```json
[
  {
    "languages": ["en", "de"],
    "cron": " 0 50 16 1/1 * ? * "
  },
  {
    "languages": ["fr", "it"],
    "cron": " 0 55 16 1/1 * ? * "
  }
]

[
  {
    "languages": ["en", "de"],
    "cron": " 1 0 0 ? * * * "
  }
]
```

### Logging

See [logback](https://logback.qos.ch/documentation.html) documentation for more details

## License

[Apache License, Version 2.0](LICENSE.md)
