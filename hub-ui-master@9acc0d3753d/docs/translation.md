# Detailed explanation of translation file
The translation file contains a JSON representation of the inputs content in a multitude of languages. 

# Table of Contents
1. Overall structure
2. Mandatoy translation properties
3. Example

## 1. Overall strcuture
The translation file needs to contain an object defining messages for different languages. Because this file may contain translation for other properties and plugins the translations for this plugin needs to be stored within an object (for each language) named dataupload:

    {
        "en": {
            "message": {
                "dataupload": {
                    "label": {
                        "title": "title"
                    }
                }  
            }
        },
        "de": {
            "message": {
                "dataupload": {
                    "label": {
                        "title": "Titel"
                    }
                }
            }
        }
    }

Within the message-property a multitude of properties can be defined matching the input properties (label, placeholder etc.)
All input fields provided trough the inputconfig contain numerous properties which can be translated. These properties are:

* label
* help
* placeholder
* add-label
* info

## 2. Mandatory translation properties
There are a few components wihtin this plugin which need translation input which is mandatory otherwise the components will be displayed malformed.

Mandatory properties to provide for proper diaply within dataupload-property:

        "dataupload": {
            "info": {
                "information": "", // header of infobox
                "defaultdescription": "" // short description within the infobox which will be displayed on default
            }
        }

## 3. Example
An example translation-file can be found [here](../config/i18n/i18n-template.json).