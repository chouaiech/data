import { has, isEmpty } from "lodash";
import dcataptypes from '../../../config/data-provider-interface/dcatap-jsonld-types';
import namespacedKeys from "../../../config/data-provider-interface/dcatap-namespace";

/**
 * Converting and saving a value as singular string
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 * @param {*} key The JSONLD name of the current property (e.g. 'dct:title')
 */
function convertSingularString(state, values, key) {
    state[key] = values;
}

/**
 * Converting and saving a value as an URI ({'@id': 'value'})
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 */
function convertSingularURI(state, values) {
    if (values.includes('@')) {
        // mail addresses need to be extended by mailto:
        state['@id'] = `mailto:${values}`;
    } else {
        state['@id'] = values;
    }
}

/**
 * Converting and saving multiple values as URIs ([{'@id': 'value1'}, {'@id': 'value2'}])
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 */
function convertMultipleURIs(state, values) {
    const arrayLength = values.length;
    for (let arrayIndex = 0; arrayIndex < arrayLength; arrayIndex += 1) {
        state[arrayIndex] = { '@id': '' };

        // the array can either contain objects with the actual value or the values directly
        if (typeof values[arrayIndex] === 'string') {
            state[arrayIndex]['@id'] = values[arrayIndex];
        } else if (typeof values[arrayIndex] === 'object') {
            state[arrayIndex]['@id'] = values[arrayIndex]['@id'];
        }
    }
}

/**
 * Converting and saving multiple values as multilingual objects ([{'@value': 'value1', '@language': 'lang1'}, {'@value': 'value2', '@language': 'lang2'}])
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 */
function convertMultiLingual(state, values) {
    const arrayLength = values.length;
    for (let arrayIndex = 0; arrayIndex < arrayLength; arrayIndex += 1) {
        // for mutilingual fields a default language is predefined and would be saved
        // it only makes sense to save this predefined (or changed) language if there is a value
        if (values[arrayIndex]['@value'] !== '' && values[arrayIndex]['@value'] !== undefined) {
            state[arrayIndex] = {'@value': '', '@language': ''};

            state[arrayIndex]['@value'] = values[arrayIndex]['@value'];
            if (values[arrayIndex]['@language']) state[arrayIndex]['@language'] = values[arrayIndex]['@language'];
        }
    }
}

/**
 * Converting and saving values depending on their conditional format
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 * @param {*} key The JSONLD name of the current property (e.g. 'dct:title')
 */
function convertConditional(state, values, key) {
    if (key === 'dct:license') {
        // licence can either be an URI or an object containing a title, description and url
        const dataKeys = Object.keys(values);
        if (typeof values === 'string') {
            state[key] = { '@id': values };
        } else if (Array.isArray(values)){
            state[key] = { "@type": "dct:LicenseDocument", "dct:title": "", "skos:exactMatch": {"@id": ""}, "skos:prefLabel": "" };
            if (values[0]['dct:title']) convertSingularString(state[key], values[0]['dct:title'], 'dct:title');
            if (values[0]['skos:exactMatch']) convertSingularURI(state[key]['skos:exactMatch'], values[0]['skos:exactMatch']);
            if (values[0]['skos:prefLabel']) convertSingularString(state[key], values[0]['skos:prefLabel'], 'skos:prefLabel');
        }
    } else if (key === 'dct:rights') {
        // rdfs:label can either be a normal String or an URI
        state[key] = {"@type": "dct:RightsStatement", "rdfs:label": ""};

        // values can either be provided in an object with a @type or as simple string
        let labelValue;
        if (typeof values === 'string') {
            labelValue = values;
        } else if (typeof values === 'object') {
            labelValue = values['rdfs:label'];
        }

        // TODO: use inbuild url checker
        if (labelValue.startsWith('www') || labelValue.startsWith('http')) {
            state[key]['rdfs:label'] = { '@id': labelValue };
        } else {
            state[key]['rdfs:label'] = labelValue;
        }
    }
}

/**
 * Converting and saving values as grouped inputs
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 * @param {*} key The JSONLD name of the current property (e.g. 'dct:title')
 */
function convertGroupedInput(state, values, key) {
    const definitions = {
        "foaf:page": {
            "@id": "", 
            "@type": "foaf:Document", 
            "dct:description": "", 
            "dct:format": {"@id": ""}, 
            "dct:title": ""
        },
        "dcat:contactPoint": {
            "@type": "", 
            "vcard:fn": "", 
            "vcard:hasAddress": { 
                "vcard:country_name": "", 
                "vcard:locality": "", 
                "vcard:postal_code": "", 
                "vcard:street_address": ""
            }, 
            "vcard:hasEmail": {"@id": ""}, 
            "vcard:hasOrganizationName": "", 
            "vcard:hasTelephone": "", 
            "vcard:hasURL": {"@id": ""}
        },
        "dct:creator": {
            "@type": "", 
            "foaf:homepage": {"@id": ""}, 
            "foaf:mbox": {"@id": ""}, 
            "foaf:name": ""
        },    
        "dct:temporal": {
            "@type": "dct:PeriodOfTime", 
            "dcat:endDate": "", 
            "dcat:startDate": ""
        },
        "dext:metadataExtension": {
            "@type": "dext:MetadataExtension", 
            "dext:isUsedBy": {"@id": ""}
        },
        "dcat:accessService": {
            "@type": 'dcat:DataService', 
            "dct:title": [{"@value": "", "@language": ""}], 
            "dct:description": [{"@value": "", "@language": ""}], 
            "dcat:endpointURL": {"@id": ""}
        },
        "dct:provenance": {
            "@id": "dct:ProvenanceStatement", 
            "rdfs:label": "",
        },
        "dct:conformsTo": {
            "@id": "", 
            "@type": "dct:Standard", 
            "rdfs:label": ""
        },
        "adms:identifier": {
            "@id": "", 
            "skos:notation": {"@type": "", "@value": ""},
        }
    };
    
    for (let index = 0; index < values.length; index += 1) {
        state[index] = definitions[key];
        addGroupedValues(state[index], values[index], definitions, key);
    }
}

/**
 * Converting and saving content and nested content of grouped input
 * @param {*} state The JSONLD-object of the store which should contain all data at the end
 * @param {*} values The actual unformatted values which should be converted and saved to the JSONLD-object (provided by UI-form)
 * @param {*} definition An object containing the JSONLD-format the current property shoul have (e.g. 'dct:title': [{'@value': '', '@laguage': ''}])
 * @param {*} parentKey The JSONLD name of the parent property (e.g. 'dct:title')
 */
function addGroupedValues(state, values, definition, parentKey) {
    const objectKeys = Object.keys(values);

    for (let keyIndex = 0; keyIndex < objectKeys.length; keyIndex += 1) {
        const key = objectKeys[keyIndex];

        if(has(definition[parentKey], key)) {
            //-------------------------------------------------
            // property with singular URI
            //-------------------------------------------------
            if (dcataptypes.nestedSingularURIs.includes(key)) {
                if (values[key]) {
                    state[key] = {};
                    convertSingularURI(state[key], values[key]);
                }
            //-------------------------------------------------
            // property with singular string value
            //-------------------------------------------------
            } else if (dcataptypes.nestedSingularString.includes(key)) {
                if (values[key]) convertSingularString(state, values[key], key);
            //-------------------------------------------------
            // nested property vcard:hasAddress (dct:contactPoint) has also grouped input
            //-------------------------------------------------
            } else if (key === 'vcard:hasAddress') {
                if (!isEmpty(values[key])) {
                    state[key] = {};
                    // second level values of address are grouped and therefore contained in an object wihtin an array
                    // these second level values are not erpetable so the array only has one element
                    const addressKeys = Object.keys(values[key][0]);
                    
                    // all address values are singulat string values
                    for (let addressIndex = 0; addressIndex < addressKeys.length; addressIndex += 1) {
                        const subKey = addressKeys[addressIndex];
                        if (values[key][0][subKey]) convertSingularString(state[key], values[key][0][subKey], subKey);
                    }
                }
                
            //-------------------------------------------------
            // conditional conversion of title and description based on parent property
            //-------------------------------------------------
            } else if (key === 'dct:title' || key === 'dct:description') {
                // title and description for page are singulat strings, for accessservice they are multilingual fields
                if (parentKey === 'foaf:page') {
                    convertSingularString(state, values[key], key);
                } else if (parentKey === 'dcat:accessService') {
                    state[key] = [{'@value': '', '@language': ''}];
                    convertMultiLingual(state[key], values[key]);
                }
            //-------------------------------------------------
            // nested property skos:notation (adms:identifier) has also grouped input
            //-------------------------------------------------
            } else if (key === 'skos:notation') {
                if (values[key]) {
                    state[key] = {};
                    // the values of skos notation are grouped and therefore stored in an array
                    // because the skos-values aren't repeatbale the array has only one element -> object containing values
                    if (values[key][0]['@value']) state[key]['@value'] = values[key][0]['@value'];
                    if (values[key][0]['@type']) state[key]['@type'] = values[key][0]['@type'];
                }
            }
        }
    }
}

/**
 * Merges jsonld data distributed within nodes
 * @param {*} nodeData Array of node data objects
 * @param {*} value Object containing value
 * @returns Object containing all data with propery namespaced keys
 */
function mergeNodeData(nodeData, value) {
    let mergedData;
    const nodeDataKeys = nodeData.map(datasets => datasets['@id']);

    // if value is a key of a danat node merge node data
    if (nodeDataKeys.includes(value)) {
        const node = replaceWithNamespaceKey(nodeData.filter(dataset => dataset['@id'] === value)[0]); // for valid jsonld the normal key names must be replcaed by namespaced keys
        mergedData = node;

        // some properties have nested grouped data which will be also located in a node and must be merged (e.g. contactPoint -> vcard:hasAddress)
        const nodeSubKeys = Object.keys(node);
        for (let nodeIndex = 0; nodeIndex < nodeSubKeys.length; nodeIndex += 1) {
        const subkey = nodeSubKeys[nodeIndex];
            if (subkey !== '@id' && nodeDataKeys.includes(node[subkey])) {
                const subnode = replaceWithNamespaceKey(nodeData.filter(dataset => dataset['@id'] === node[subkey])[0]); // for valid jsonld the normal key names must be replcaed by namespaced keys
                mergedData[subkey] = subnode;
            }
        }
    }
    return mergedData;
}

/**
 * Replaces normal key names with namespaced keys
 * @param {*} dataObject Object of data with normal keys
 * @returns Object of data with namespaced keys
 */
function replaceWithNamespaceKey(dataObject) {
    const namespacedObject = {};
    const normalKeys = Object.keys(dataObject);

    for (let index = 0; index < normalKeys.length; index += 1) {
        let key;
        // @id and @type already are proper keys so they don't need conversion
        if (normalKeys[index] !== '@id' && normalKeys[index] !== '@type') {
            key = namespacedKeys[normalKeys[index]];
        } else {
            key = normalKeys[index];
        }

        if (!isEmpty(key)) {
            // if namespaced key is an URI-property convert the value into an URI
            if (dcataptypes.nestedSingularURIs.includes(key)) {
                namespacedObject[key] = {'@id': dataObject[normalKeys[index]]};
            } else {
                namespacedObject[key] = dataObject[normalKeys[index]];
            }
        }
    }
    return namespacedObject;
}

export default { 
    convertSingularString, 
    convertSingularURI, 
    convertMultipleURIs, 
    convertMultiLingual, 
    convertConditional, 
    convertGroupedInput, 
    mergeNodeData, 
    replaceWithNamespaceKey 
};
