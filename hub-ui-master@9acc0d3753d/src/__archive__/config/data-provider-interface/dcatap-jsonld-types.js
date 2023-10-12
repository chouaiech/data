// Converted to JSONLD each property of DCAT-AP has to have a specific format
// There are multiple properties having similar formats so they got summarized into the following objects and arrays for using the fitting value conversion 
// function based on the format the later JSONLD should have

// all DCAT-AP properties (first level) which will be presented by a singular string
const singularString = {
    datasets: [
        "dct:catalog",
        "dct:issued",
        "dct:modified",
        "dcat:spatialResolutionInMeters",
        "owl:versionInfo",
    ],
    distributions: [
        "dct:issued",
        "dct:modified",
        "dcat:byteSize",
        "dcat:spatialResolutionInMeters",
    ],
    catalogues: [],
};

// all DCAT-AP properties (first level) which will be presented by a singular URI ({'@id': ''})
const singularURI = {
    datasets: [
        "dct:publisher",
        "dct:accrualPeriodicity",
        "dct:spatial",
        "dct:accessRights",
    ],
    distributions: [
        "dct:format",
        "dct:type",
        "dcat:mediaType",
        "dcat:availability",
        "dcat:compressFormat",
        "dcat:packageFormat",
        "adms:status",
    ],
    catalogues: [
        "dct:isPartOf",
        'foaf:homepage',
    ],
};

// all DCAT-AP properties (first level) which will be presented by a multitude of URIs ([{'@id': ''}, ...])
const multipleURI = {
    datasets: [
        "dct:language", // array of string values
        "dct:subject",  // array of string values
        "dcat:theme", // array of string values
        "dct:type", // array of string values
        "dct:source", // array of objects {'@id': '...'}
        "dcat:landingPage", // array of objects {'@id': '...'}
        "dct:relation", // array of objects {'@id': '...'}
        "dcat:qualifiedRelation", // array of objects {'@id': '...'}
        "prov:qualifiedAttribution", // array of objects {'@id': '...'}
        "dct:isReferencedBy", // array of objects {'@id': '...'}
        "prov:wasGeneratedBy", // array of objects {'@id': '...'}
        "dct:isVersionOf", // array of objects {'@id': '...'}
        "dct:hasVersion", // array of objects {'@id': '...'}

    ],
    distributions: [
        "dcat:accessURL",
        "dcat:downloadURL",
        "dct:language",
        "odrl:hasPolicy",
    ],
    catalogues: [
        "dct:hasPart",
        'dcat:catalog',
    ],
}

// all DCAT-AP properties (first level) which will be presented by a multitude of values with a language ([{'@value': '', '@language': ''}, ...])
const multiLang = {
    datasets: [
        "dct:title",
        "dct:description",
        "dcat:keyword",
        "adms:versionNotes",
    ],
    distributions: [
        "dct:title",
        "dct:description",
    ],
    catalogues: [],
};

// all DCAT-Ap properties (first level) which have different JSONLD formats based on the conditional form input
const conditionalValues = {
    datasets: [],
    distributions: [
        "dct:license",
        "dct:rights"
    ],
    catalogues: [
        "dct:license",
        "dct:rights"
    ],
};

// all DCAT-AP properties (first level) which contain a multitude of second level properties
const groupedProperties = [
    'foaf:page',
    'dcat:contactPoint',
    'dct:creator',
    'dct:temporal',
    'dext:metadataExtension',
    'dcat:accessService',
    // 'adms:identifier', // uncomment as soon as backend error is fixed
    'dct:provenance',
    'dct:conformsTo',
];

// all DCAT-AP properties which are nested within a first level property and which JSONLD presentation will be a singular URI ({'@id': ''})
const nestedSingularURIs =  [
    'dct:format',
    'vcard:hasEmail',
    'vcard:hasURL',
    'foaf:homepage',
    'foaf:mbox',
    'dext:isUsedBy',
    'spdx:algorithm',
    'dcat:endpointURL',
];

// all DCAT-AP properties which are nested within a first level property and which JSONLD presentation will be a singular string
const nestedSingularString = [
    'vcard:fn',
    'vcard:hasOrganizationName',
    'vcard:hasTelephone',
    'foaf:name',
    'dcat:endDate',
    'dcat:startDate',
    'spdx:checksumValue',
    "vcard:country_name",
    "vcard:locality", 
    "vcard:postal_code", 
    "vcard:street_address",
    'rdfs:label',
    '@id',
    '@type',
];

export default { singularString, singularURI, multipleURI, multiLang, conditionalValues, nestedSingularURIs, nestedSingularString, groupedProperties };
