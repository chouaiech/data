# Detailed explanation of user configuration
This document provides a detailed explanation of all parameters which need or could be set within the configuration-file.

# Table of Contents
1. General structure
2. Input properties
3. Formatter
4. Example


## 1. General structure
The user configuration file is mandatory and contains information about language settings and sources from where to retrieve data for inputs.

        const generalConfig = {
            // contains all information about data sources for each input property defined in inputconfig-file
            //!ATTENTION: property need to be named 'input'
            input: {
                //MANDATORY: method handling returned JSON-LD-data
                uploadMethod: {
                    method: "" // provide a method for uploading the retuned JSON-LD-data
                },
                //MANDATORY: properties need to match property names from plugin call
                jsonldendpoint: {
                    dataset: "https://yoururl.com/${id}/...",
                    catalogue: "https://yoururl.com/${id}/..."
                }
                //Attention: you should provide the id within the url as a variable. 
            },

            // MANDATORY: information about language settings
            languages: {
                locale: '', // provide language code (ISO-639-1)
                fallbackLocale: '', // provide language code (ISO-639-1)
                translations: '' // object of translations provided trough translation-file
            }
        }

## 2. Input properties
There is the possibility to use input types which require data (select etc.). This data can be provided directly at the inputconfig-file or indirectly trough the definition of a parameter-name at the 'source'-property linking to settings defined at the generalconfig-file (see explanation of [inputconfig](./inputconfig.md)). This property defined within the input-section of the generalconfig-file contains five parameters describing the method of trteiving data and its source.

There are four different cases to differentiate when setting sources:

* Case 1: There is a local source from which the data can be retrived trough an import-statement and this data is already matching the format requirements. This would lead to the following settings:

        import data from 'path-to-data'
        ...

        dataTheme: {
            fetchFromEndpoint: false,
            endpoint: "",
            values: data,
            needsFormatting: false,
            formatter: ""
        }   

* Case 2: There is a local source from which the data can be trrieved trough an import statement but this datat needs frther formatting. You can either use on of the provided formatters (see section 3) or write an own formatter and import it to the file. This would lead to the following settings:

        import data from 'path-to-data'
        import dataformatter from 'path-todataformatter'
        ...

        dataTheme: {
            fetchFromEndpoint: false,
            endpoint: "",
            values: data,
            needsFormatting: true,
            formatter: dataformatter
        }

* Case 3: The data needs to be treieved from an endpoint but is matching the demanded formatting. This would lead to the following settings:

        dataTheme: {
            fetchFromEndpoint: true,
            endpoint: "https://.....",
            values: "",
            needsFormatting: false,
            formatter: ""
        }

* Case 4: The data needs to be retreived from an endpoint ans also needs formatting. This would lead to the follwoing settings:

        import dataformatter from 'path-to-dataformatter'
        ....

        dataTheme: {
            fetchFromEndpoint: true,
            endpoint: "https://.....",
            values: "",
            needsFormatting: true,
            formatter: dataformatter
        }

# 3. Formatter
The data neede by an input should match a normal object structure (key-value-pairs). If the provided source doesn't match this structure a formatter-method can be provided to convert the provided data into the demanded structure. The formatter needs to take two input parameters and return an object containing the data as key-value-pairs.

        function formatterName(data, languageInformation){

            // extraction of data (in wanted language) to object

            return dataObject
        }

| Parameter        | Description           | 
| ------------- |-------------| 
| data      | Parameter of any type containing the data to format | 
| languageInformation      | Parameter of type Object caontaining the language settings from the generalconfig-file (local, fallback, translation). Note: This is needed to provide a multilingual content but can be ignored, if needed.      | 

There are also inbuild formatters ready for usage. To use them just provide the name of the formatter tought the formatter-property.

List of inbuild formatters:
* DataEuropa

# 4. Example
An example configuration file can be found [here](../config/generalconfig-template.js)
