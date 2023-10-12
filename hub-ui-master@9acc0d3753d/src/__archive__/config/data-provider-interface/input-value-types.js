// all first level properties which format for JSONLD and the input form are identical so th evalues can be just copied
const sameFormatProperties = {
    datasets: [
        'dct:title',
        'dct:description',
        'dcat:keyword',
        'dct:issued',
        'dct:modified',
        'adms:versionNotes',
        'dct:source',
        'dcat:landingPage',
        'dct:relation',
        'dcat:qualifiedRelation',
        'prov:qualifiedAttribution',
        'dcat:spatialResolutionInMeters',
        'dct:temporal',
        'dct:isReferencedBy',
        'prov:wasGeneratedBy',
        'dct:isVersionOf',
        'dct:hasVersion',
        'owl:versionInfo',
        'dct:provenance',
        'dct:conformsTo',
        'dct:catalog',
    ],
    distributions: [
        'dct:title',
        'dct:description',
        'dct:issued',
        'dct:modified',
        'dcat:byteSize',
        'dcat:spatialResolutionInMeters',
        'odrl:hasPolicy',
        'dct:conformsTo',
        'dcat:accessURL',
        'dcat:downloadURL',
    ],
    catalogues: [],
};

// JSONLD contains an array o URIs which needs to be converted into an array of strings (URLs)
const multiURIs = {
    datasets: [
        'dct:language',
        'dct:subject',
        'dcat:theme',
        'dct:type',
    ],
    distributions: [
        'dct:language',
    ],
    catalogues: [],
}

// JSONLD contains singular URI which needs to be converted to a singular string (URL)
const singularURI = {
    datasets: [
        'dct:publisher',
        'dct:spatial',
        'dct:accrualPeriodicity',
        'dct:accessRights',
    ],
    distributions: [
        'dct:format',
        'dct:type',
        'dcat:mediaType',
        'dcat:availability',
        'dcat:compressFormat',
        'dcat:packageFormat',
        'adms:status',
    ],
    catalogues: [],
}

// values which multiple different value types and nested values
const groupedValues = {
    datasets: [
        'foaf:page',
        'dcat:contactPoint',
        'dct:creator',
        'dext:metadataExtension',
        'adms:identifier',
    ],
    distributions: [
        'dcat:accessService',
        'foaf:page',
    ],
    catogues: [],
}

// properties which are nested and URIs
const nestedSingularURIs = [
    'foaf:homepage',
    'foaf:mbox',
    'dct:format',
    'dext:isUsedBy',
    'vcard:hasURL',
    'vcard:hasEmail',
    'dcat:endpointURL',
];

export default { sameFormatProperties, multiURIs, singularURI, groupedValues, nestedSingularURIs };
