# Detailed explanation of input configuration
The demanded input configuration structure is based on the [VueFormulate schema](https://vueformulate.com/guide/forms/generating-forms/#schemas) and expanded to support DCAT-AP. Especially the demanded structure and naming of inputs is designed to enable convertion of provided input into JSON-LD.

# Table of Contents
1. Overall structure
2. Supported input types
3. Input properties  
    3.1 Type  
    3.2 Label (Placeholder, Help, Add-Label, Info)  
    3.3 Name property
    3.4 Options property (and source)  
    3.5 Validation property  
    3.6 @change property  
    3.7 Class property
4. Conditional inputs
5. Example



## 1. Overall structure
The component provides the ability to create a multi-step-wizard for a multitude of configurations. Therefore there is the possibility to define multiple objects containing an array of objects describing the inputs to be rendered within the inputconfig-file. Navigation and basic validation is already included and can be extended.

Each distinct main wizard must have a distinct configuartion object which is defined in the inputconfig file and needs to be imported at the main.js file during the plugin-call of the DCAT-AP-fronten library (see [README 3. Usage](../README.md)).

Following the definition of inputs will be described on one example configuration.

Each configuration must be an Array containing Objects describing the content of each wizard-page. So each objects describes a distinct page/step within the wizard. Each of the object needs to contain a route-property defining the name of the route the wizard.page will be displayed at. Furthermore this object must also contain a type-property with the value of 'group' and a children-property which contains an Array of Objects wich described the inputs displayed on the page.

        const exampleConfig = [
            {
                route: "page1",
                name: "dataset_1/catalogue_1/distribution_1"
                type: "group",
                children: [
                    {...}, {...}, {...}
                ]
            },
            {
                route: "page2",
                name: "dataset_2/catalogue_2/distribution_2",
                type: "group",
                children: [
                    {...}, {...}, {...}
                ]
            },
            ...
        ];

        export {exampleConfig}

!Attention: It is very important to define a name for each page which describes the DCAT-AP-profile the data should be assigned to. There are (at the moment) three possibilities: 'dataset', 'distribution' or 'catalogue'. Please use these prefixes when defining the page and an iteration_marker (e.g. _1). Only when each page has a sufficient name the jsonld-converter can convert the provided input into JSON-LD.

## 2. Supported input types
Many input types provide a specialized type with inbuild validation. For further information see the provided link.
* text (https://vueformulate.com/guide/inputs/types/text/)
* textarea (https://vueformulate.com/guide/inputs/types/textarea/#textarea-2)
* button (https://vueformulate.com/guide/inputs/types/button/)
* radio and checkboxes (https://vueformulate.com/guide/inputs/types/box/)
* file upload (https://vueformulate.com/guide/inputs/types/file/)
* group (https://vueformulate.com/guide/inputs/types/group/)
* select (https://vueformulate.com/guide/inputs/types/select/)
* slider (https://vueformulate.com/guide/inputs/types/sliders/)
* searchable select (custom component using [vue-select](https://vue-select.org/))
* conditional-input (custom component using)

## 3. Input properties

To define an input there is a list of properties which can be used to define the type and behavior of the input. There is a huge list of supported inputs and input-props provided by the vue-formulate-libary (see https://vueformulate.com/guide/inputs/#props).

### 3.1 Type

The type property determines the type of the input which should be rendered. There is a multitude of supproted types by vue-formulate (see section 2).

There is a special kind of type called 'group' which bundles a group of inputs together. This group can either be epeatable or not. This can be determined by providing attional properties:

    // normal definition of an inputs type
    {
        type: "text"
    }

    // definition of a group of inputs (not repeatable)
    {
        type: "group",
        children: [
            {...}, {...}
        ]
    }

    // definition of repeatable group of inputs
    {
        type: "group",
        repeatable: true,
        "add-label": "Add group",
        children: [
            {...}, {...}
        ]
    }

### 3.2 Label (Placeholder, Help, Add-Label, Info)

The label property determines the label of an input. For this library this property should contain a string dtermining a property-key matching a translation provided trough the translation file. So on loading of the page all inputs will be translated according to the selected language and displayed.

    // definition of input with label
    {
        type: "text",
        label: "title",
        ...
    }

    // translation file content which will be used to translate the label
    {
        "en": {
            "message": {
                "label": {
                    "title": "Title"
                }
            }
        },
        "de": {
            "message": {
                "label": {
                    "title": "Titel"
                }
            }
        },
        ...
    }

The naming and translation of the properties 'placeholder', 'add-label', 'help' and 'info' works the same and requires a corresponding key within the translation file.

The placeholder determines a message or example etc. within the input to demonstrate the wanted input content.

The help property defines a message serving as help to fill in the input.

The add-label contains the description/label of the button of a repeatable group which will add another instance of the group.

The info property contains a detailed explanation of each field which will be displayed within the info-box next to the input on click of the info-symbol.

### 3.3 Name property
The name property serves as basis for the later convertion of the provided data into JSON-LD supporting the [DCAT-AP schema](https://www.dcat-ap.de/def/dcatde/1.1/spec/specification.pdf). To guarantee proper conversion the demanded grouping structure and naming needs to be kept.

Each property should be named according to DCAT-AP using a compact IRI (example: dcat:distribution). Futhermore typed values should be provided with the type attribute.

Example for naming a group property (top; all other needed properties were deleted for better overview) and single property:

        {
            "type": "group",
            "label": "title",
            "name": "dct:title", 
            "repeatable": true,
            "add-label": "title",
            "children": [
                {
                    "name": "@value",
                    "type": "text",
                    "label": "title",
                    ...
                },
                {
                    "name": "@language",
                    "type": "select",
                    "label": "language",
                    ...
                }
            ]
        },
        {
            "type": "text",
            "name": "dct:description 
        }

Resulting JSON-LD:

        // single title provided
        "dct:title": {
            "@language": "en", // ISO-code of chosen language
            "@value": "Lorem ipsum" //submitted title
        }

        // multiple titles provided
        "dct:title": [
            {
            "@language": "en",
            "@value": "Title of a very interesting publication"
            },
            {
            "@language": "de",
            "@value": "Titel einer sehr interessanten Veröffentlichung"
            }
        ]

It is important to keep the demanded structure and provide the correct naming, otherwise the JSON-LD converter will produce questionable results.

### 3.4 Options property (and source)
Input types like selects or checkboxes need a set of data to display. This data must be provided matching the follwong schema:

    {
        "key": "value",
        "key": "value",...
    }

There are two ways of providing data to inputs. The first possibility is to provide the object directly as value of the options-property in the inputconfig-file. 

The second possibility is to provide an object to the source-property containing a name linking to further source information located at the generalcinfig-file. This information is used to automatically fetch or read the data from the given source. Note: If the given source doesn't match the needed format a converter needs to be provided or one of the inbuild converters can be used.

For further explanation see [generalconfig](../config/generalconfig-template.js)

Example:

    // inputconfig-file
    ...
    "source": { "name": "dataTheme"}

    // generalconfig-file
    ...
    dataTheme: {
        "fetchFromEndpoint": true,
        "endpoint": "https://data.europa.eu/api/hub/search/vocabularies/data-theme",
        "value": "",
        "needsFormatting": true,
        "formatter": "DataEuropa"
    }

    // important: name in inputconfig must match name in generalconfig!

### 3.5 Validation property

All typed of inputs have a basic inbuild validation. If an input needs additional validation there is a large set of rules which can be applied to an input (see [available validation rules](https://vueformulate.com/guide/validation/#available-rules)).

The needed validation an input needs to check can be provided trough the validation-property:

    // single rule
    {
        type: "text",
        validation: "required"
    }

    // multiple rules
    {
        type: "text",
        validation: ["required", "ends_with:oad,ode"]
    }

There is also a property handling the display of error-messages. There are three types of error-display-behaviors (see [error-behavior](https://vueformulate.com/guide/validation/#error-behavior)):

* live: Always shows applicable error messages
* submit: Shows applicable error messages when the form is submitted
* blur: Shows applicable error messages when a user's focus leaves a field

The wanted behavior an be defined by using the 'error-behavior' property. If no behavior is defined the default one is the 'blur' behavior.

Recommended behavior: 'live'

### 3.6 @change property

This property should be used on all inputs at the deepest nesting state to track changes in the input. All changes will be saved immediatly for later use within the JSON-LD converter. Also it provides the ability to save defined inputs and their content to the localStorage which later can be used as prefilled input for users.

    // add to all deep nested inputs which changes should be tracked ans saved
    {
        type: "group",
        children: [
            {
                type: "text",
                @change: true
            },
            {
                type: "select",
                @change: true
            }
        ]
    }

### 3.7 Class property

The class-property contains all CSS-classes which should be applied to the given input. 

    // apply property-class to input
    {
        type: "text",
        class: "property"
    }

There are a few predefined classes:

* property: nice padding and bordering of input
* besides: Class defining a container which contents flow side by side
* main: Sub-class of besides. Determines the main component which should take the most space
* sub: Sub-class of besides. Determines the subordinate component which should take less space

## 4. Conditional inputs
Conditional inputs are dynamic inputs which change on a users input. For example to display either a select field or a number of fields to fill in text.
Conditional inputs have an own type which can be called similar to all other types. The input need a normal options-object providing possibilities to choose between. Furthermore there is a new property named 'data' whcih needs to be provided. The data-property contains an object with the same keys as defined in the options-property. The value of these keys are array containing objects of input definitions similar to the normal definitions.
All other properties need to be provided similar to the 'normal' input types. Don't forget about the @change-property!

        {
            type: "conditional-input",
            label: "publisher",
            name: "dct:publisher",
            options: {
                uri: "Choose an URI", 
                manual: "Manual provide information"
            },
            "@change": true,
            data: {
                uri: [
                    {
                        type: "select",
                        label: "...",
                        name: "...",
                        options: {},
                        source: {name: publishers}
                    }
                ],
                manual: [
                    {
                        type: "group",
                        name: "...",
                        label: "...",
                        children: [
                            {
                                type: "text",
                                name: "...",
                                label: "title",
                                ...
                            },
                            {
                                type: "url",
                                label: "homepage",
                                name: "...",
                                ...
                            },
                            ...
                        ]
                    }
                ]
            }

        }

Also fetching of values from endpoints or files is possible similar to 'normal' input types.

## 5. Example
Find an example of an inputconfig-file [here](../config/inputconfig-template.json)

