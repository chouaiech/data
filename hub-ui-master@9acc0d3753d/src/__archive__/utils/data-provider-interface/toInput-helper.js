import { isEmpty } from 'lodash';
import inputtypes from '../../../config/data-provider-interface/input-value-types';

/**
 * Converts array of URIs into Array of strings
 * @param {*} state 
 * @param {*} values 
 * @param {*} key
 */
function multiUriToString(state, values, key) {
    const stringArray = [];

    for (let arrayIndex = 0; arrayIndex < values.length; arrayIndex += 1) {
        const currentValue = values[arrayIndex];
        stringArray.push(currentValue['@id']);
    }
    state[key] = stringArray;
}

/**
 * Conerted JSONLD grouped values into input grouped values matching form input format
 * @param {*} state 
 * @param {*} values 
 * @param {*} key 
 */
function convertGroupedProperties(state, values, key) {

    if (key === 'spdx:checksum') {
        state[key] = {};
        convertNestedProperties(state[key], values);
    } else {
        state[key] = [];
        for (let arrayIndex = 0; arrayIndex < values.length; arrayIndex += 1) {
            state[key][arrayIndex] = {};
            convertNestedProperties(state[key][arrayIndex], values[arrayIndex]);
        }
    }

}

/**
 * Converts nested properties to input format
 * @param {*} state 
 * @param {*} values 
 */
function convertNestedProperties(state, values) {
    const objectKeys = Object.keys(values);

    for (let keyIndex = 0; keyIndex < objectKeys.length; keyIndex += 1) {
        const key = objectKeys[keyIndex];
        
        if (inputtypes.nestedSingularURIs.includes(key)) {
            if (!isEmpty(values[key])) {
                // mail addresse are extended with mailto: which needs to be removed for the input
                if (values[key]['@id'].startsWith('mailto:')) {
                    state[key] = values[key]['@id'].replace('mailto:', '');
                } else {
                    state[key] = values[key]['@id'];
                }
            }
        } else if (key === 'skos:notation') {
            state[key] = [{}];
            const subKeyArray = Object.keys(values[key]);
            for (let subIndex = 0; subIndex < subKeyArray.length; subIndex += 1) {
                const subkey = subKeyArray[subIndex];
                if (!isEmpty(values[key][subkey])) {
                    state[key][0][subkey] = values[key][subkey];
                }
            }
        } else if (key === 'vcard:hasAddress') {
            state[key] = [{}];
            const subKeyArray = Object.keys(values[key]);
            for (let subkeyIndex = 0; subkeyIndex < subKeyArray.length; subkeyIndex += 1) {
                const subkey = subKeyArray[subkeyIndex];
                if (!isEmpty(values[key][subkey])) {
                    state[key][0][subkey] = values[key][subkey];
                }
            }
        } else {
            if (!isEmpty(values[key])) {
                state[key] = values[key];
            }
        }
    }
    
}

export default { multiUriToString, convertGroupedProperties };
