# DCAT-AP Frontend
A frontend component for providing metadata as a multi-step-wizard according to the DCAT application profile.
The input structure gets derived from a configuration file determining, which fields are needed.
All provided data gets converted to JSON-LD.

# Table of Contents
1. Installation
2. Requirements
3. Usage
4. Configuration

    4.1 Input configuration

    4.2 User configuration

    4.3 Translation

5. Included Formatters
6. Local development

## 1. Installation
Is available at the private npm registry https://paca.okd.fokus.fraunhofer.de/repository/npm-hosted.

    npm i @piveau/dcatap-frontend

## 2. Requirements
* Input configuration file
* User configuration file
* Content translation file
* vue-router instance
* vuex store instance
* the main application should provide vue-i18n


## 3. Usage
Setup the plugin in main.js in order to register DCATAPInput components globally:

    // In main.js
    import DCATAPInput from 'dcatap-frontend';
    import Vuex from 'vuex';
    import VueRouter from 'vue-router';

    // support of multiple configurations provided within the input-config-file
    import {config1, config2...} from 'inputconfig-path';
    import user-config from 'user-config-path';

    import store from 'location-of-store;
    import router from 'location-of-router;

    // use of DCATAP-Frontend component
    // plugin import supports multiple input-configurations
    // !ATTENTION: Note that the parameter keys have to be exactly like shown ('inputconfig', 'store', 'generalconfig', 'router')

    // using the provided css
    require('@piveau/dcatap-frontend/dist/dcatap-frontend.css');

    // ATTENTION! The configuration names used as keys HAVE TO match the given names 'dataset' or 'catalogue' (other profiles might be added at some point)
    Vue.use(DCATAPInput, { 
      inputconfig: {
        dataset: config1, 
        catalogue: config2
      }, 
      store: store, 
      generalconfig: user-config,
      router: router
    });

    new Vue({
      store,
      router,
      render: h => h(App),
    }).$mount('#app');

To utilize the now available component you have to create another component in your application which uses the plugin-component and also receives a property trough the router:
Utilize the now available component:

    // inside the newly defined component within you application
    
    <template>
      <div>
        <DCATAPInput :configurationName="name"/> 
      </div>
    </template>

    export default {
      name: MyNewComponentWichUsesDCATAP,
      props: ['name'],
    }

Furthermore you have to define a new route within your router with the name "DCATAP-Upload" whith an dynamic route depedning on the 'name'-property:

    // define new routes within the router:
    {
      path: "upload/:name",
      name: "DCATAP-Upload",
      component: MyNewComponentWichUsesDCATAP,
      props: true,
    }

The last step for utilizing the component is to link to it providing the configuration to use:

    <router-link 
    :to="/upload/configName/nameOfFirstRoute">
      Link to DCATAP-Upload view
    </router-link>

You only need to provide the main route name. A redirect to the first subroute will happen automatically

Furthermore there is the possibility to edit given data. Therefore you have to provide endpoints in the generalconfig and use the given route to trigger fetching of data and usage of this data as input values.

    <router-link :to="/edit/profile/id">Edit dataset </router-link>

 
## 4. Configuration
### 4.1 Input configuration
The input configuration contains one or multiple JSON representations of form fields which should be displayed.
The formatting and content of the input configuration is very complex and therefore described here: [general input explanation](../docu/inputconfig.md)

For an example configuration see: [example input configuration file](./inputconfig-template.js)

### 4.2 General configuration
Provides additional configuration details and input sources. The content and its definition is explained here: [general config explanation](../docu/generalconfig.md)

An example configuration file is provided here: [example configuration file](./config/user-config-template.js)

### 4.3 Translation
The translation file contains a JSON representation of all used properties in a multutide of languages. 

* [general translation explanaition](../docu/translation.md)
* [example translation file](./config/i18n/i18n-template.json)


## 5. Included Formatters
  * DataEuropa

## 6. Local development
To further develop this library locally some preparations need to be done.

Step 1: Clone this repository and change into its directory and install all neccessary dependencies.

    git clone https://gitlab.fokus.fraunhofer.de/piveau/hub/piveau-hub-forms
    cd piveau-hub-forms
    npm install

Step 2: Inside the piveau-hub-forms-directory create a new vue2-projects (for example called 'demo') which uses vue-router, vuex and vue-i18n.  
!!REMEMBER: Add this new projects folder to the .gitignore!!!

Step 3: Change into the demo projects package.json and add the library to the dependencies-section.

    dependencies: {
      "dcatap-frontend": "../",
      ...
    }

Step 4: Configure the main.js of your demo project as well as provide suitable configuration and translation-files.

Step 5: Change back to the parent directory (piveau-hub-forms) and build the package locally:

    npm run build:lib

Step 6: As soon as the package is build. Change back to the directory of the demo project, install all dependencies and run the local server:

    cd demo
    npm i
    npm run serve // or npm run dev

