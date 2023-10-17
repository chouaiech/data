# piveau-hub-ui-modules

> Please use node version >= 16. Recommended is version 17.x.

This is a space to develop Vanilla piveau-hub-ui components. 
The contents of the modules folder can be published as an npm package to our npm registry
which can then be used in Piveau based projects to import the
required parts. It also has a sample-app which can be run as follows:

## Running the sample app

1. Make sure you have a configuration file named `user-config.js` in the `config` folder.
To start with, you can make a copy of `user-config.sample.js` and rename it to 
`user-config.js`.
2. Run the command `npm ci` to install the dependencies.
3. We must add the contents of the modules folders as a package dependency in node_modules. You have the following possibilities:
    - Either you run `npm run localdeploy`; then the modules will be bundled and copied into node_modules. (Note that changes in the modules folders will cause no hot-reloads in this case -- you have to run this command over again to reflect changes.)
    - Or: you run `npm run link-module`. A Symlink to the modules folder will then be added to node_modules. IMPORTANT: if you work under Windows, you must execute that command as admin! Alternatively you could also create the symlink by hand (adapt your path):
      ```
      MKLINK /d   "D:\Dev\piveau\piveau-hub-ui-modules\node_modules\@piveau\piveau-hub-ui-modules" "D:\Dev\piveau\piveau-hub-ui-modules\src\modules"
      ```
4. Run the command `npm run serve` and open http://localhost:8080/ in your browser.

## Project setup
```
npm ci
```

### Test app compiles and hot-reloads for development
```
npm run serve
```

### Compiling modules
```
npm run build
```

or to compile and also add a copy of the compiled modules in node_modules:

```
npm run localdeploy
```

### Creating a symlink of the module in node_modules
```
npm run link-module
```

### Publishing modules
```
npm run deploy
```

### Run your unit tests
```
npm run test:unit
```

### Run your end-to-end tests
```
npm run test:e2e
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
