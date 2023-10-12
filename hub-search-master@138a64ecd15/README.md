# Hub Search

You know, for search. (tbc)

## Table of Contents

***

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Deployment](#deployment) 
5. [API](#api)
6. [CLI](#cli)
7. [Key Decisions](#key-decisions)
8. [Demo](#demo)
9. [Code Quality](#code-quality)
10. [Known Issues](#known-issues)
11. [Further Reading](#further-reading)
12. [Maintainer](#maintainer)
13. [License](#license)


## Prerequisites

***

Install all of the following software:

 * [Apache Maven](https://maven.apache.org/) >= 3
 * [OpenJDK](http://openjdk.java.net/) >= 11
 * [Elasticsearch](https://elastic.co) = 7.10.2


## Installation

***

1. Spin up an instance of Elasticsearch.

2. Check if Elasticsearch is running:

```bash
$ curl -X GET localhost:9200
{
  "name" : "17e915ce4b34",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "gI8uM9MVTjqX5FTphaHbiA",
  "version" : {
    "number" : "7.10.2",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "747e1cc71def077253878a59143c1f785afa92b9",
    "build_date" : "2021-01-13T00:42:12.435326Z",
    "build_snapshot" : false,
    "lucene_version" : "8.7.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

3. Create the configuration file

```bash
$ cp conf/config.sample.json conf/config.json
```

4. Edit the configuration file to your requirements, see [Configuration](#configuration).

5. Build the application

```bash
$ mvn clean package
```

6. Run the application

```bash
$ java -jar target/search.jar
```

7. Browse to `http://localhost:8080` for the api specification

#### Integration Tests

Test files are split in `UnitTest` and `IntegrationTest`. 
The naming convention for integration tests is `[.*IntegrationTest.*]`.
Integration tests are excluded from normal build.
If you want to run the integration tests, use:

```bash
$ mvn test -Dtest=*IntegrationTest
```

**Note**: You need a running elasticsearch instance on port 9200 to run the integration tests.

## Configuration

***

Configuration is done by setting the environment variables or by using a configuration file `conf/config.json.sample` listed below. 
Environment variables are prioritized over configuration parameters set in the configuration file.
If child parameters of a json based configuration parameter are missing the result is merged with default values and/or parameters of the respective other configuration type.

| Key                                     | Description                                | Type     | Default                   | Required |
| :-------------------------------------- | :----------------------------------------- | :------- | :------------------------ | :------- |
| `PIVEAU_HUB_SEARCH_API_KEY`             | API key for accessing write operations.    | `string` | -                         | Yes      |
| `PIVEAU_HUB_SEARCH_ES_CONFIG`           | Elasticsearch configuration.               | `json`   | - | Yes      |
| &#8594; `.host`                         | Host of the elasticsearch endpoint.        | `string` | localhost                 | no       |
| &#8594; `.port`                         | Port of the elasticsearch endpoint.        | `integer`| 9200                      | no       |
| &#8594; `.scheme`                       | Protocol of the elasticsearch endpoint.    | `string`| http                      | no       |
| &#8594; `.circuitBreakerTries`          | Request retry limit via circuit breaker of the elasticsearch client. (Currently only used for startup ping)                                                                                  | `integer`| 10                        | no       |
| &#8594; `.index`                        | Index configuration                        | `json`   | -                          |  no      |
| &#8594; `.index.*`                      | Replace `*` with configured index: <br /> `dataset`, `catalogue`, `dataservice` or `vocabulary`                                                                           | `json`   | -                          | no       |
| &#8594; `.index.*.max_agg_size`         | Maximum size for term aggregation .        | `integer`| 50                        |  no      |
| &#8594; `.index.*.max_result_window`    | Defines a result limit for pagination. <br /> (page*limit+limit > max_result_window) | `integer`| 10000                     |  no      |
| &#8594; `.index.*.settings`             | Path to index setting file.                | `string` | -       |  yes        |
| &#8594; `.index.*.mapping`              | Path to index mapping file.                | `string` | -       |  yes        |
| &#8594; `.index.*.facets`               | A list of facets and their configuration. <br /> More details can be found in [Facet Configuration](#facet-configuration)  | `jsonarray` | empty       |  no        |
| &#8594; `.index.*.boost`               | Configure which fields get a boosted score while searching.                    | `json`   | -        | no         |
| &#8594; `.index.*.boost.{field}`                      | The field that will be boosted by the configured value. | `integer`| -        | no         |   
| &#8594; `.index.*.searchParams`         | A list of search parameters to field or values mappings. | `jsonarray` | empty      |  no        |
| &#8594; `.index.*.searchParams.name` | The name of the search parameter.      | `string`  | -        |  no           |
| &#8594; `.index.*.searchParams.field` | The field of the mapped search parameter.  | `string`  | -        |  no           |
| &#8594; `.index.*.searchParams.values` | The values of the mapped search parameter.  | `string`  | -        |  no           |
| &#8594; `.vocabularyReplacements`       | A list of field for vocabulary replacements.   | `jsonarray` | empty        |  no           |
| &#8594; `.vocabularyReplacements.field` | The field to be replaced with vocabulary.  | `string`  | -        |  no           |
| &#8594; `.vocabularyReplacements.vocabulary` | The vocabulary to be used for replacement.  | `string`  | -        |  no           |
| `PIVEAU_HUB_SEARCH_SERVICE_PORT`        | HTTP port number this service will run on. | `integer`| `8080`  | no       |
| `PIVEAU_HUB_SEARCH_CLI_CONFIG`          | CLI configuration.                         | `json`   | -       | no         |
| &#8594; `.port`                         | CLI port.                                  | `integer`| `8081`  | no         |
| &#8594; `.type`                         | CLI type. (currently only http is supported)| `string`| `http`  | no         |
| `PIVEAU_HUB_SEARCH_GAZETTEER_CONFIG`    | Gazetteer configuration.                   | `json`   | -       | no      |
| &#8594; `.type`                         | Gazetteer type.                            | `string` | conterra | no       |
| &#8594; `.url`                          | Gazetteer url.                             | `string` | -        | no      |
| `PIVEAU_IMPRINT_URL` |URL to imprint page. Used for OpenAPI GDPR complience| `string` |`/`| no       |
| `PIVEAU_PRIVACY_URL` |URL to privacy policy page. Used for OpenAPI GDPR complience| `string` |`/`| no       |
| `PIVEAU_HUB_SEARCH_FEED_CONFIG` |Feed config| `json` |-| no       |
| &#8594; `.title`                          | Feed title.                             | `string` | `data.europa.eu`       | no      |
| &#8594; `.relative_path_datasets`                          | Path for datasets.                           | `string` | `/data/datasets/`       | no      |
| &#8594; `.relative_path_search`                          | Path for search.                    | `string` | `/data/search`       | no      |
| &#8594; `.relative_path_datasets_api_endpoint`                          | Path for datasets api endpoint.                            | `string` | `/api/hub/search/datasets/`       | no      |

#### Facet Configuration

| Key                                     | Description                                | Type     | Default                   | Required |
| :-------------------------------------- | :----------------------------------------- | :------- | :------------------------ | :------- |
| `name`                                  | Name of the facet.                         | `string` | -                         | yes       |
| `title`                                 | Title of the facet.                        | `string` | -                         | yes       |
| `path`                                  | Path to the field of the facet.            | `string` | -                         | yes       |
| `type`                                  | Define facet type. If nothing selected the result is a facet with term aggregation. <br /> Select: `nested`,`min`, `max`, `range`, `mustMatch` or `mustNotMatch` | `string` | -                         | no       |

##### Nested Facet

Summarizes facets that belong together. 

| Key                                     | Description                                | Type     | Default                   | Required |
| :-------------------------------------- | :----------------------------------------- | :------- | :------------------------ | :------- |
| `facets`                                | List of included facets. Recursive definition, see above for more details. | `jsonarray` | empty                         | no       |

##### Min Facet

Finds the documents with the minimum value of a field defined via `path`. For more details see: [Min Aggregation](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-min-aggregation.html)

##### Max Facet

Finds the documents with the maximum value of a field defined via `path`. For more details see: [Max Aggregation](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-max-aggregation.html)

##### Range Facet

Counts the documents in a given range of a field defined via `path`. For more details see: [Range Aggregation](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-range-aggregationhtml)

| Key                                     | Description                                | Type     | Default                   | Required |
| :-------------------------------------- | :----------------------------------------- | :------- | :------------------------ | :------- |
| `from`                                  | Lower bound for range aggregation. (inclusive)        | `integer`| -                         | yes      |
| `to`                                    | Upper bound for range aggregation. (exclusive)        | `integer`| -                         | yes      |

##### MustMatch Facet

Counts the documents that match the specified values of a field defined via `path`. For more details see: [Filter Aggregation](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-filter-aggregation.html)

| Key                                     | Description                                | Type     | Default                   | Required |
| :-------------------------------------- | :----------------------------------------- | :------- | :------------------------ | :------- |
| `match`                                 | Define whether the values must match or must not match. | `boolean`| -                         | yes      |
| `values`                                | List of values to be matched.              | `jsonarray`| -                       | yes      |

**Hint**: Use when the list of values to be matched is clearly defined. 

##### MustNotMatch Facet

Counts the documents that do not match the specified values of a field defined via `path`. For more details see: [Filter Aggregation](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-filter-aggregation.html)

**Hint**: Use when the list of values to be matched is not clearly defined but the list of values to be not matched is. 

| Key                                     | Description                                | Type     | Default                   | Required |
| :-------------------------------------- | :----------------------------------------- | :------- | :------------------------ | :------- |
| `match`                                 | Define whether the values must not match or must match. | `boolean`| -                         | yes      |
| `values`                                | List of values to be not matched.              | `jsonarray`| -                       | yes      |

## Deployment

***

Deployment should be done using [Docker](https://www.docker.com/) containers. 
Changes to the `master` and `develop` branch are deployed automatically.
View the `.gitlab-ci.yml` file for details.

## API

***

A formal OpenAPI 3 specification can be found in the `src/main/resources/webroot/openapi.yaml` file.
A visually more appealing version is available at `{url}:{port}` once the application has been started.

## CLI

### Connect:

You should be able to connect either via the browser (http) or via shell (telnet).

Via browser:
```bash
http://localhost:8081/shell.html
```

Via telnet:
```bash
$ telnet localhost 5000
```

### Display commands:

The `listCommands` lists all available custom commands. For more enter `help`.
To display all available custom commands:
```bash
% listCommands
```

### Index configurations:

#### Reset all indices:

The `resetIndices` command deletes all available indices and initializes all indices with the specified configuration.
It brings all indices to the initial state as if the hub search was started for the first time.
To reset all indices, enter:
```bash
% resetIndices
```

#### Create an index:

The `createIndex` command creates an index. 
To create an index, enter:

```bash
% createIndex index
```

If you like to specify the number of shards, enter:

```bash
% createIndex index numberOfShards
```

#### Example:

```bash
% createIndex dataset_230223-000001 3
```

#### Remove an index:

The `removeIndex` command removes an index.
To remove an index, enter:
```bash
% removeIndex index
```

###### Example:

```bash
% removeIndex dataset_230223
```

#### Set the number of replicas for an index:

The `setNumberOfReplicas` command sets the number of replicas for an index.
To set the number of replicas for an index, enter:
```bash
% setNumberOfReplicas index positiveInteger
```

**Note:** This command also allows aliases, e.g. `dataset_write`.

###### Example:

```bash
% setNumberOfReplicas dataset_write 2
```

#### Set the mapping of an index:

The `setMapping` command sets the mapping for an index.
To set the mapping for an index, enter:
```bash
% setMapping index
```

**Note:** This command also allows aliases, e.g. `dataset_write`.

###### Example:

```bash
% setMapping dataset_write
```

#### Set read-alias of an index:

The `setReadAlias` command sets the read-alias for an index.
To set the read-alias for an index, enter:
```bash
% setReadAlias index
```

###### Example

```bash
% setReadAlias dataset_230223-000001
```

#### Set write-alias of an index:

The `setReadAlias` command sets the write-alias for an index.
To set the write-alias for an index, enter:
```bash
% setWriteAlias index
```

###### Example

```bash
% setWriteAlias dataset_230223-000001
```

#### Set max_result_window for an index:

The `setMaxResultWindow` command sets the maximum result window for an index.
To set the maximum result window for an index, enter:
```bash
% setMaxResultWindow index positiveInteger
```

**Note:** This command also allows aliases, e.g. `dataset_write`.
**Note:** The value must be greater than `10000`.

###### Example

```bash
% setMaxResultWindow dataset_read 1500000
```

### Query configurations

#### Set the boost value for a query filter:

The `boostField` command sets the boost value of a field for a query filter.
To set the boost value of a field for a query filter, enter:
```bash
% boostField filter field floatValue
```

**Note**: The query filter is used for the search api to filter for an index prefix.

###### Example:

```bash
% boostField dataset title 10.0
```

#### Set max_agg_size for a query filter:

The `setMaxAggSize` command sets the maximum aggregation size for a query filter.
To set the maximum aggregation size for a query filter, enter:
```bash
% setMaxAggSize index positiveInteger
```

**Note**: The query filter is used for the search api to filter for an index prefix.

###### Example:

```bash
% setMaxAggSize dataset 100
```

### Misc

#### Trigger sitemap generation

The `triggerSitemapGeneration` command triggers the sitemap generation procedure.
To trigger the sitemap generation procedure, enter:
```bash
% triggerSitemapGeneration
```

#### Index xml vocabularies

The `indexXmlVocabularies` command indexes xml vocabularies.
In particular, `iana-media-types` and `spdx-checksum-algorithm`
To index xml vocabularies, enter:
```bash
% indexXmlVocabularies
```

#### Reindex catalogues

The `reindexCatalogues` command reindexes all catalogues.
To reindex all catalogues, enter:
```bash
% reindexCatalogues
```

#### Sync scores

The `syncScores` command allows to sync scores between `dataset` aliases.
For this to make sense, read and write alias must point to different indices.
To sync scores, enter:
```bash
% syncScores
```

#### Reset webroot

The `resetWebroot` command allows to removes the webroot folder under `./conf/webroot`.
This webroot folder is created whenever openapis are generated based on shacl files.
To remove the webroot folder, enter:
```bash
% syncScores
```

## Key Decisions

### About indices, index prefixes and aliases

Initially hub-search creates indices with following naming convention `PREFIX_DATE-NUM`.
Where:
* PREFIX can be either `dataset`, `catalogue`, `dataservice` or `dataset-revisions`.
* DATE is in format `YYMMDD` states the date of the index.
* NUM is a sequential number starting at `000001`. (This is used for the revisions rollover)

Example: `dataset_230223-000001`

Each of the initial indices has aliases with following naming convention `PREFIX_ALIAS`.
Where:
* ALIAS can be either `read` or `write`.

Example: `dataset_read`

### About vocabulary indices

The vocabulary indices are simply named `vocabulary_ID`. Here the index prefix is `vocabulary`. 
For the vocabulary indices no aliases are used.

## Demo

***

tbc


## Code Quality

***

The `develop` branch is evaluated for code quality [here](https://sonarqube.apps.osc.fokus.fraunhofer.de/dashboard?id=io.piveau.hub%3Asearch).


## Known Issues

***

tbc


## Further Reading

***

tbc

## Maintainer

***

[Anton Altenbernd](mailto:anton.altenbernd@fokus.fraunhofer.de)

## License

***

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
