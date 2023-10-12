# Configuring the user-config.js file

The user-config.js file defines which baseUrl and adapter implementations will be used by the core-application.
It exports a JavaScript Object that contains the information about the Backend base Url to fetch data from and the locations of your implemented adapters.

##### 1. See the provided [user-config.js example file](https://gitlab.fokus.fraunhofer.de/viaduct/piveau-ui/piveau-ui/-/blob/DEU-develop/config/user-config.js) and copy the code into your user-config.js file.

##### 2. Change the import paths to the locations of your implemented adapters (see adapters guide [this guide](https://gitlab.fokus.fraunhofer.de/apertos/apertos-frontend/blob/master/guides/adapters-guide.md)) 

##### 3. Change the String in api.baseUrl to the base Url of the Backend-API you want to fetch data from.

##### 4. Ensure the files name is user-config.js and your file structure looks like this:
myProject<br />
|-- apertos-frontend<br />
|-- myAdapters<br />
|-- user-config.js<br />
|-- custom_vars.sass<br />
`-- custom_bulma_variables.sass<br />
