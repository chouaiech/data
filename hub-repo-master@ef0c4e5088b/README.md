# piveau hub

This is the core module for the piveau data platform. It manages syncs the triple store and the search index and provides a rich RESTful API.

## Required Piveau Services 

In order to run the hub requires some other services

- Virtuoso Triplestore (Mandatory)
- piveau-search (Highly recommended)
- piveau-shacl-validation (Recommended)
- piveau-data-upload (Recommended)
- piveau-translation-service (Optional)
- Keycloak (Optional)
- Virtuoso Shadow Triplestore (Optional)
  - The shadow triplestore stores hidden content that must not be accessible through virtuoso's public API
  - This includes:
    - Dataset drafts
    - Metrics history 

## Prerequisites

- Java JDK 17
- Maven 3
- Docker and Docker Compose

## Setup
- Clone the repository and navigate into the directory
```
$ cp conf/config.sample.json conf/config.json
$ docker-compose up -d
```
- Wait a couple of seconds. 
- Check if the search service initialised properly by browsing to http://localhost:8081
- If an error is displayed, restart the search service `docker-compose restart piveau-search`
- Start the hub
- Windows:
```
$ redeploy.bat
```
- Linux/MacOS:
```
$ ./redeploy.sh
```
- or
```
$ mvn package exec:java
```
- Browse to http://localhost:8080

## Docker Compose

- The provided Docker Compose file includes all services for local setup and development
- You can start it either entirely or select required services
- Currently, it does not include the translation service and the Keycloak

## Build and Run with Docker

build:
```bash
$ mvn clean package
$ sudo docker build -t=hub .
```

run:
```bash
$ sudo docker run -p 8080:8080 -d piveau-hub
```
## Configuration 
- A sample configuration can be found in [conf/config.sample.json](conf/config.sample.json)
- The sample configuration works well with the provided docker-compose file

| Name                                                                       | Description                                                       | Type                  |
|----------------------------------------------------------------------------|-------------------------------------------------------------------|-----------------------|
| PIVEAU_HUB_SERVICE_PORT                                                    | The port for the service                                          | number                |
| PIVEAU_HUB_API_KEY                                                         | The API key of the service                                        | string                |
| PIVEAU_HUB_API_KEYS                                                        | A map of API keys associated with a list of resources             | json                  |
| PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA.clientId                             | Client ID of backend instance                                     | string                |
| PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA.clientSecret                         | Client secret of backend instance                                 | string                |
| PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA.tokenServerConfig                    | Token server config                                               | json                  |
| PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA.tokenServerConfig.keycloak           | Keycloak config                                                   | json                  |
| PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA.tokenServerConfig.keycloak.realm     | Keycloak realm name                                               | string                |
| PIVEAU_HUB_AUTHORIZATION_PROCESS_DATA.tokenServerConfig.keycloak.serverUrl | Keycloak Host                                                     | string                |
| PIVEAU_HUB_BASE_URI                                                        | The RDF base url                                                  | string                |
| PIVEAU_HUB_FORCE_UPDATES                                                   | Forcing update on put dataset                                     | boolean               |
| PIVEAU_TRIPLESTORE_CONFIG.address                                          | URL of the triplestore                                            | string                |
| PIVEAU_TRIPLESTORE_CONFIG.data_endpoint                                    | Relative CRUD endpoint of the triplestore                         | string                |
| PIVEAU_TRIPLESTORE_CONFIG.query_endpoint                                   | Relative query endpoint of the triplestore                        | string                |
| PIVEAU_TRIPLESTORE_CONFIG.username                                         | Username for the triplestore                                      | string                |
| PIVEAU_TRIPLESTORE_CONFIG.password                                         | Password for the triplestore                                      | string                |
| PIVEAU_SHADOW_TRIPLESTORE_CONFIG.address                                   | URL of the shadow triplestore                                     | string                |
| PIVEAU_SHADOW_TRIPLESTORE_CONFIG.username                                  | Username for the shadow triplestore                               | string                |
| PIVEAU_SHADOW_TRIPLESTORE_CONFIG.password                                  | Password for the shadow triplestore                               | string                |
| PIVEAU_HUB_VALIDATOR.enabled                                               | Enable the use of the validator                                   | bool                  |
| PIVEAU_HUB_VALIDATOR.history                                               | Enable metrics history                                            | bool                  |
| PIVEAU_HUB_VALIDATOR.metricsPipeName                                       | Name of the validation pipe                                       | string                |
| PIVEAU_HUB_SEARCH_SERVICE.enabled                                          | Enable the use of the indexing                                    | string                |
| PIVEAU_HUB_SEARCH_SERVICE.url                                              | Host of the piveau-search service                                 | string                |
| PIVEAU_HUB_SEARCH_SERVICE.port                                             | Port of the piveau-search service                                 | number                |
| PIVEAU_HUB_SEARCH_SERVICE.api_key                                          | API key of the piveau-search service                              | string                |
| PIVEAU_HUB_LOAD_VOCABULARIES                                               | Enable the loading of RDF vocabularies                            | bool                  |
| PIVEAU_HUB_LOAD_VOCABULARIES_FETCH                                         | Enable the loading of RDF vocabularies from remote                | bool                  |
| PIVEAU_TRANSLATION_SERVICE.enable                                          | Enable the machine translation service                            | bool                  |
| PIVEAU_TRANSLATION_SERVICE.accepted_languages                              | Target languages to be translated                                 | array                 |
| PIVEAU_TRANSLATION_SERVICE.translation_service_url                         | URL of the translation service                                    | string                |
| PIVEAU_TRANSLATION_SERVICE.callback_url                                    | URL of the callback for the translation service                   | string                |
| PIVEAU_DATA_UPLOAD.url                                                     | URL of the data upload service                                    | string                |
| PIVEAU_DATA_UPLOAD.service_url                                             | Base URL of the download URL for the data                         | string                |
| PIVEAU_DATA_UPLOAD.api_key                                                 | API key of the data upload service                                | string                |
| PIVEAU_HUB_CORS_DOMAINS                                                    | Remote URLs, without protocol, that are allowed to access the hub | JSON Array of strings |
| PIVEAU_HUB_CORS_DOMAINS                                                    | Remote URLs, without protocol, that are allowed to access the hub | JSON Array of strings |
| PIVEAU_FAVICON_PATH                                                        | Path to a favicon. Can be a web resource                          | string or URL         |
| PIVEAU_LOGO_PATH                                                           | Path to a logo. Can be a web resource                             | string or URL         |
| PIVEAU_IMPRINT_URL                                                         | URL to imprint page. Used for OpenAPI GDPR complience             | URL                   |
| PIVEAU_PRIVACY_URL                                                         | URL to privacy policy page. Used for OpenAPI GDPR complience      | URL                   |
| greeting                                                                   | Meaningless string                                                | string                |

## Known Issues

### Elasticsearch
- It may be possible that Elasticsearch won't start, which requires some tweaks. Please refer to this documentation: https://gitlab.fokus.fraunhofer.de/viaduct/viaduct-hub-search/wikis/how-to-deploy-elasticsearch

## License

[Apache License, Version 2.0](LICENSE.md)

