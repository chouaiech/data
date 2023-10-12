# Piveau UI

## Project Setup

Install [Node.js 8 <= v14 and NPM 6.10.0+](https://nodejs.org/en/) on your system

Clone or download the Piveau-UI repository:

    $ git clone git@gitlab.fokus.fraunhofer.de/piveau/hub/piveau-hub-ui.git

Install NPM packages:

    $ cd piveau-ui
    $ npm install

Make sure to provide a valid configuration file `config/user-config.js`.
We also provide an example configuration file `config/user-config.example.js` for usage. To get started immediately, it is recommended to copy and paste the content of `config/user-config.example.js` into `config/user-config.js`.

### Development

Open a terminal in the `piveau-ui` directory and run:

    $ npm run dev

<br>This will start a local webserver on Port `8080` (unless you defined a different port in `piveau-ui/config/index.js`).
<br>Open a web browser and visit `http://localhost:8080` to see the app.
<br>Hot Module Replacement is supported. The page will update automatically whenever files are changed and saved.

If there is an error similar to `Unable to resolve path to module '../config/user-config'  import/no-unresolved`, usually doing the following steps resolves this:
1. make sure to stop the running development console (`CTRL + C`)
2. make sure a configuration file exists in `config/user-config.js`
3. remove the folder `node_modules/.cache`
4. start the development console `npm run dev`

### Testing

We use Cypress for all types of tests. The relevant files are all stored in the /cypress folder. There exist two different test runners: one for isolated component testing (i.e. unit testing) and one for integration and end-to-end testing.

The main difference between component testing and integration testing is that in component testing you import and mount a component and perform actions and assertions directly on that component. In contrast, integration and end-to-end testing is performed by programmatically visiting a navigation link to a page and perform tests on that page.

Integration tests are stored in /tests and unit tests in /tests/unit. The /src folder structure should be mirrored in /tests/unit. All test files must have the extension `.spec.js`

More Information on how to write Cypress tests can be found here:
* [Integration tests](https://docs.cypress.io/guides/overview/why-cypress)
* [Unit tests](https://docs.cypress.io/guides/component-testing/introduction)


**Cypress Integration Test Runner**

Cypress runs integration tests in a unique interactive runner that allows you to see commands as they execute while also viewing the application under test.

Install Cypress
```
npm install cypress --save-dev
```
Run
```
npm run cy
```

to open the Cypress Integration Test Runner. Tests can be executed there.

Headless integration testing:

Run

```
npm run cy:run:firefox
```

**Cypress Component Test Runner**

To open the Cypress Component Test Runner, run the following command:

```
npm run cy:unit
```

Headless version:

```
npm run cy:unit:headless
```


### Build for Production

Open a terminal in the `piveau-ui` directory and run:

    $ npm run build

If you're on Windows, you've probably encountered an error like:
```
'NODE_ENV' is not recognized as an internal or external command, operable program or batch file.
```

Run:
```
   $ npm install -g win-node-env
```
<br>This will optimize files for production and store the bundle in `piveau-ui/dist`
<br>Deploy the contents of `piveau-ui/dist` on your webserver.

<br>

### Run it via Docker

- Build the application for production
```
$ docker build -t piveau-ui .
$ docker run -i -p 8080:8080 piveau-ui
```

<br>

## Configurations

The `piveau-ui` consists of several configuration files for users and developers.

### User Configurations

**Note:** _Environment variables created by the [Runtime Configurations](#runtime-configurations) will always override the corresponding configurations from `user-config.js` when used correctly!_

#### user-config.js
The user-config.js file is located at `piveau-ui/config/` by default. It is the main project configuration file. The following example file shortly describes the configurable values.

<details>
<summary>Open user-config.js Example File</summary>

```javascript

const glueConfig = {
  title: 'data.europa.eu',
  description: 'data.europa.eu',
  keywords: 'DEU',
  api: {
    baseUrl: 'https://data.europa.eu/data/search/',
    similarityBaseUrl: 'https://data.europa.eu/api/similarities/',
    gazetteerBaseUrl: 'https://data.europa.eu/data/search/gazetteer/',
    uploadBaseUrl: 'https://data.europa.eu/data/api/',
    matomoUrl: 'https://data.europa.eu/piwik/',
    authToken: '',
    vueAppCorsproxyApiUrl: 'https://piveau-corsproxy-piveau.apps.osc.fokus.fraunhofer.de/?uri=',
  },
  keycloak: {
    enableLogin: true,
    realm: 'edp',
    url: 'https://www.europeandataportal.eu/auth',
    'ssl-required': 'external',
    clientId: 'edp-ui',
    'public-client': true,
    'verify-token-audience': true,
    'use-resource-role-mappings': true,
    'confidential-port': 0,
  },
  rtp: {
    grand_type: 'urn:ietf:params:oauth:grant-type:uma-ticket',
    audience: 'piveau-hub',
  },
  locale: 'en',
  fallbackLocale: 'en',
  services: {
    catalogService,
    datasetService,
    distributionService,
    datastoreService,
    gazetteerService,
    uploadService,
    authService,
  },
  themes: {
    header: 'dark',
  },
  routerOptions: {
    base: '/data',
    mode: 'history',
  },
  navigation: {
    top: {
      main: {
        home: {
          // href: 'https://link-to-external-url.com' (optional)
          // target: ['_self' | '_blank'] (optional)
          show: true,
        },
        data: {
          show: true,
        },
        maps: {
          show: false,
        },
        about: {
          show: false,
        },
        append: [
          {
            href: 'https://www.fokus.fraunhofer.de/datenschutz',
            target: '_self',
            title: 'Privacy Policy',
          },
          {
            href: 'https://www.fokus.fraunhofer.de/9663f8cb2d267d4b',
            target: '_self',
            title: 'Imprint',
          },
        ],
        icons: false,
      },
      sub: {
        privacyPolicy: {
          show: false,
          href: 'https://www.fokus.fraunhofer.de/datenschutz',
          target: '_self',
        },
        imprint: {
          show: false,
          href: 'https://www.fokus.fraunhofer.de/9663f8cb2d267d4b',
          target: '_self',
        },
      },
    },
    bottom: {}
  },
  images: {
    top: [
      {
        src: 'https://i.imgur.com/lgtG4zB.png',
        // href: 'https://my-url.de'(optional)
        // target: ['_self' | '_blank'] (optional)
        description: 'Logo data.europa.eu',
        height: '60px',
        width: 'auto',
      },
    ],
    bottom: [],
  },
};

```

</details>

### runtime-config.js
The `runtime-config.js` file is located at `piveau-ui/config/` by default. It is a template file, which lists all configurable environment variables that can be changed during runtime. See [Runtime Configurations](#runtime-configurations) for more information.

#### lang folder / i18n.js
The `lang` folder and `i18n.js` file are located at `piveau-ui/config/i18n/` by default. The `lang` folder contains translations for all available languages. The `i18n.js` imports all language files of the `lang` folder and exports them as one object.


### Developer Configurations

#### index.js
The `index.js` file is located at `piveau-ui/config/` by default and is generated by the Vue-Webpack-Bundle. It contains several configurations for the development and production build process.

#### custom_theme.scss
The `custom-theme.scss` file is located at `piveau-ui/src/styles/` by default. It contains Bootstrap 4 SCSS variables and overrides the default Bootstrap values. It must be used to change any general styling rules like spacing, sizes, colors etc. It is also possible to add new color variables or other new variables, which can then be used via Bootstrap classes.

<br>

### Runtime Configurations

**Note:** _Runtime Configurations are only applied, when running the application via [Docker](#run-it-via-docker)_

We utilize a Vue plugin `RuntimeConfiguration` to configure a web application using environment variables without rebuilding it.

See [runtime-config.js](config/runtime-config.js) for all available runtime variables.

#### Create new runtime variables

**Note:** _Just like the default configuration, runtime configurations (or environment variables) will be loaded client-side. Therefore, it is recommended that you **do not** store sensitive information like passwords or tokens._

Configuration variables are only changeable during runtime if bind to an environment variable by following specific rules.

To do so, follow these steps:
1. In [runtime-config.js](config/runtime-config.js), add the desired configuration variable as a property and enter it´s environment variable name as value. However, there are still some restrictions:
    -  While the variable name can be chosen freely, it must have the prefix `$VUE_APP`.
    -  The property should be consistent in it´s name *and* structure.
2.  Build and deploy the application.
3.  Set the environment variable *without* the dollar sign `$` at the beginning, e.g. if the new entry in [runtime-config.js](config/runtime-config.js) is `MATOMO_URL: '$VUE_APP_MATOMO_URL'`, make sure that the environment variable `VUE_APP_MATOMO_URL` is set accordingly.

#### Example

Let's suppose `process.env` looks like this (depending on how the project is set up):
```
{
  NODE_ENV: 'production',
  ROOT_API: 'https://data.europa.eu/api',
  ROOT_URL: 'https://data.europa.eu',
  MATOMO: {
      API_URL: 'https://data.europa.eu/piwik/',
  }
}
```

and we want to change `ROOT_API` and add a new property `MATOMO.ID` during runtime. Let's go through the steps outlined above:

1.  Add new property `MATOMO.ID` to [runtime-config.js](config/runtime-config.js):
```
export default {
  ROOT_API: '$VUE_APP_ROOT_API',
  ROOT_URL: '$VUE_APP_ROOT_URL',
  MATOMO: {
      API_URL: '$VUE_APP_MATOMO_API_URL',
      ID: '$VUE_APP_MATOMO_ID'
  }
}
```
2.  Build and deploy.
3.  Set the environment variables `VUE_APP_ROOT_API` and `VUE_APP_MATOMO_ID`:
```
VUE_APP_ROOT_API=https://data.europa.eu/newApi
VUE_APP_MATOMO_ID=89
```
