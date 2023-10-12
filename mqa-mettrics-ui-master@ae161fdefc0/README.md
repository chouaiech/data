# metrics-ui

> meta quality dashboard project

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# run all tests
npm test
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

## Configuration Reference

Configurations are stored inside `config` as `dev.env.js` and `prod.env.js`. Refer to `example.env.js` for an example configuration file.

Configurations can be accessed in code by using

```
this.$env.{KEY}
// or
Vue.prototype.$env.{KEY}
```

| Key                | Description                                                                   |
|--------------------|-------------------------------------------------------------------------------|
| NODE_ENV           | Which Node environment to use (development or production)                     |
| ROOT_API           | Base URL for API requests. Should point to a metrics-cache API.               |
| ROOT_URL           | The service URL of this application, e.g., `https://europeandataportal.eu/`   |
| MATOMO_URL         | Matomo Analyze backend URL                                                    |
| DEBUG_DEV          | Used for debug logging                                                        |
| SCORING_START_DATE | Start date for the rating evolution graph formatted as ISO, e.g, `2020-01-01` |
| SCORING_STEP_SIZE  | Step size for the data value axis of the rating evolution graph               |
| SCORING_MAX_POINTS | Maximum value of the data value axis of the rating evolution graph            |
| HISTORY_START_DATE | Start date for all historic graphs formatted as ISO, e.g., `2020-01-01`       |
| HISTORY_RESOLUTION | Resolution of the historic graphs, e.g., `month`       |

## Runtime Configuration

We utilize a Vue plugin `RuntimeConfiguration` to configure a web application using environment variables without rebuilding it.

See [runtime-config-template.js](./src/utils/runtimeconfig/runtime-config-template.js) for all available runtime variables.

### Usage

Normally, you would use `process.env` to consume environment specific configuration. This changes here.
To consume environment variables in a Vue component, use `this.$env` as you would use `process.env`.

#### Example

```
this.$env.MATOMO_URL // Formerly process.env.MATOMO_URL
```

`this.$env` differs from `process.env` in a way that some properties that are set using environment variables are set here. That means that in a development environment, `this.$env` would be equivalent to `process.env`.

### Creating new runtime variables

If you want to make configuration properties dynamic during runtime using environment variables, follow these steps:

1. In [runtime-config-template.js](./src/utils/runtimeconfig/runtime-config-template.js), add your desired configuration variable as a property and enter its enivronment variable name as value. However, there are some restrictions you must follow:
    -  Environment variable names in [runtime-config-template.js](./src/utils/runtimeconfig/runtime-config-template.js) must have the prefix `$VUE_APP_`.
    -  Ensure that the property you want to replace during runtime is consistent in its name *and* structure.
2.  Build and deploy the application.
3.  Set your environment variable *without* the dollar sign `$` at the beginning. For example, if your new entry in [runtime-config-template.js](./src/utils/runtimeconfig/runtime-config-template.js) is `MATOMO_URL: '$VUE_APP_MATOMO_URL'`, make sure you set the environment variable `VUE_APP_MATOMO_URL`.
4.  Your  variable is now ready for consumption and can be changed during runtime by changing their associated environment variable.

#### Example

Let's suppose `process.env` looks like this (depending on how the project is set up):
```
{
  NODE_ENV: 'production',
  ROOT_API: 'https://www.europeandataportal.eu/metrics',
  ROOT_URL: 'https://www.europeandataportal.eu',
  MATOMO: {
      API_URL: 'https://www.ppe-aws.europeandataportal.eu/piwik/',
      ID: 89
  }
}
```

and you want to change `ROOT_API`, `MATOMO.API_URL`, and `MATOMO.ID` during runtime. Let's go through the steps outlined above:

1.  [runtime-config-template.js](./src/utils/runtimeconfig/runtime-config-template.js), would look like this:
```
export default {
  ROOT_API: '$VUE_APP_ROOT_API',
  MATOMO_URL: {
      API_URL: '$VUE_APP_MATOMO_API_URL',
      ID: '$VUE_APP_MATOMO_ID'
  }
}
```
2.  Build and deploy.
3.  Now set the environment variables. These are: `VUE_APP_ROOT_API`, `VUE_APP_MATOMO_API_URL`, and `VUE_APP_MATOMO_ID`.
