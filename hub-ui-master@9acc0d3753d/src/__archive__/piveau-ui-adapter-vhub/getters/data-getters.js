/* eslint-disable padded-blocks */
import {
  has, isNil, isArray, isString, isNumber, isObject,
} from 'lodash';

/**
 * Converts an array of strings or an array of objects ({key, default} pairs)
 * to an object with the array's elements as keys initialized to a default value
 */
function convertKeyArrayToObject(keys) {
  if (!keys) return undefined;

  return keys.reduce((map, obj) => {
    const key = obj.key || obj;
    const defaultValue = obj.default || undefined;
    map[key] = defaultValue; // eslint-disable-line
    return map;
  }, {});
}

const getters = {
  /*
  ***** GENERIC METHODS TO TRANSFORM DATA MODEL *****
  */
  getString: (parent, property) => {
    // Initialize empty string
    let string;

    // Check if property exists and if it is a non-empty string
    if (has(parent, property) && isString(parent[property]) && !isNil(parent[property])) string = parent[property];

    // Return the value or an empty string
    return string;
  },
  getNumber: (parent, property) => {
    // Initialize empty string
    let number;

    // Check if property exists and if it is a non-empty number
    if (has(parent, property) && isNumber(parent[property]) && !isNil(parent[property])) number = parent[property];

    // Return the value or an empty number
    return number;
  },
  getObject: (parent, property, keys) => {
    // Initialize default object
    // The initial object contains all keys set to undefined,
    // matching the behavior from v1.2.4 for backward compatibility
    const object = convertKeyArrayToObject(keys) || {};

    // Check if property exists and if it is an object
    if (has(parent, property) && isObject(parent[property])) {
      // For each key in the object
      // keys can be of shape ['key1', 'key2', ...]
      // or [{key: 'key1', default: undefined}, {key: 'key2', default: undefined}, ...]
      // make sure to cleanly iterate through all key strings.
      for (const key of keys.map(k => k.key || k)) {

        // Check if property.key exists and if it is not null
        if (has(parent[property], key) && !isNil(parent[property][key])) object[key] = parent[property][key];
      }
    }

    // Return the value or an empty object
    return object;
  },
  getObjectLanguage: (parent, property, placeholder) => {
    // Declare object
    let object;

    // Check if property exists and if it is an object
    if (has(parent, property) && isObject(parent[property])) {

      // Initialize empty object
      object = {};

      // For each key in the object
      Object.keys(parent[property]).forEach((key) => {

        // Check if property.key exists and if it is not null
        if (has(parent[property], key) && !isNil(parent[property][key])) object[key] = parent[property][key];
      });
    } else if (placeholder) object = { en: placeholder };

    // Return the value or the placeholder object
    return object;
  },
  getArrayOfNumbers: (parent, property) => {
    // Initialize empty array
    const array = [];

    // Check if property exists and if it is an array
    if (has(parent, property) && isArray(parent[property])) {

      // For each element of the array
      for (const element of parent[property]) {

        // Check if element is a non-empty string
        if (!isNil(element) && isNumber(element)) array.push(element);
      }
    }

    // Return the value or an empty array
    return array;
  },
  getArrayOfStrings: (parent, property) => {
    // Initialize empty array
    const array = [];

    // Check if property exists and if it is an array
    if (has(parent, property) && isArray(parent[property])) {

      // For each element of the array
      for (const element of parent[property]) {

        // Check if element is a non-empty string
        if (!isNil(element) && isString(element)) array.push(element);
      }
    }

    // Return the value or an empty array
    return array;
  },
  getArrayOfObjects: (parent, property, keys) => {
    // Initialize empty array
    const array = [];

    // Check if property exists and if it is an array
    if (has(parent, property) && isArray(parent[property])) {

      // For each element of the array
      for (const element of parent[property]) {

        // Check if element is a non-empty object
        if (!isNil(element) && isObject(element)) {

          // Initialize empty object
          const object = {};

          // Set all keys to undefined
          for (const key of keys) {

            // Check if element.key exists and if it is not null
            if (has(element, key) && !isNil(element[key])) object[key] = element[key];
          }

          // Add the object if it is not empty
          if (!isNil(object)) array.push(object);
        }
      }
    }

    // Return the value or an empty array
    return array;
  },
  /*
  ***** SPECIFIC METHODS TO TRANSFORM DATA MODEL *****
  */
  getCount: (parent) => {
    let count = 0;
    if (has(parent, 'count') && !isNil(parent.count)) count = parent.count;
    return count;
  },
  getDistributions: (parent) => {
    const distributions = [];
    if (has(parent, 'distributions') && isArray(parent.distributions)) {
      for (const d of parent.distributions) {
        let distribution;
        if (!isNil(d) && isObject(d)) {
          distribution = d;
          distributions.push(distribution);
        }
      }
    }
    return distributions;
  },
  getOriginalLanguage: (parent) => {
    let originalLanguage;
    if (has(parent, 'translation_meta') && has(parent, 'translation_meta.details') && !isNil(parent.translation_meta.details) && isObject(parent.translation_meta.details)) {
      Object.keys(parent.translation_meta.details).forEach((key) => {
        if (has(parent.translation_meta.details[key], 'original_language')) originalLanguage = parent.translation_meta.details[key].original_language;
      });
    }
    return originalLanguage;
  },
  getTranslationMetaData: (parent) => {
    const translationMetaData = {
      fullAvailableLanguages: [],
      details: {},
      status: undefined,
    };
    if (!has(parent, 'translation_meta')) return translationMetaData;
    if (isNil(parent.translation_meta) || !isObject(parent.translation_meta)) return translationMetaData;
    if (has(parent, 'translation_meta.full_available_languages') && !isNil(parent.translation_meta.full_available_languages)) {
      for (const l of parent.translation_meta.full_available_languages) {
        if (!isNil(parent.translation_meta.full_available_languages[l])) translationMetaData.fullAvailableLanguages.push(l);
      }
    }
    if (has(parent, 'translation_meta.details') && !isNil(parent.translation_meta.details)) {
      Object.keys(parent.translation_meta.details).forEach((key) => {
        if (!isNil(parent.translation_meta.details[key])) translationMetaData.details[key] = parent.translation_meta.details[key];
      });
    }
    if (has(parent, 'translation_meta.status') && !isNil(parent.translation_meta.status)) translationMetaData.status = parent.translation_meta.status;
    return translationMetaData;
  },
};
export default getters;
