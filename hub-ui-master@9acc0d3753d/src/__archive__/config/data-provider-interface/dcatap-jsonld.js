// the following objects contain the complete set of properties for each DCAT-AP profile with an empty representation of the base level data structure the property should have
// These objects serve as JSONLD blueprint for the dpi store where the properties get filled with values, a context gets added and the resulting valid JSONLD gets saved to 
// the backend

const datasets = {
    "@id": "", // ""
    "@type": "dcat:Dataset",
    "adms:sample": "", // will remain empty and filled by backend 
    "dcat:distribution": [], // [{"@id": ""}]
    "dct:title": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }] 
    "dct:description": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }]
    "dct:catalog": "", // ""
    "dct:publisher": {}, // {"@id": ""} 
    "dcat:keyword": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }]
    "dct:issued": "", // ""
    "dct:modified": "",  // ""
    "dcat:contactPoint": [], // [{"@type": "", "vcard:fn": "", "vcard:hasAddress": { "vcard:country_name": "", "vcard:locality": "", "vcard:postal_code": "", "vcard:street_address": ""}, "vcard:hasEmail": {"@id": ""},
         // "vcard:hasOrganizationName": "", "vcard:hasTelephone": "", "vcard:hasURL": {"@id": ""}, }]
    "dct:creator": [], // [{"@type": "", "foaf:homepage": {"@id": ""}, "foaf:mbox": {"@id": ""}, "foaf:name": ""}]
    "dct:language": [], // [{"@id": ""}]
    "dct:subject": [], // [{"@id": ""}]
    "dcat:theme": [], // [{"@id": ""}]
    "dct:type": [], // [{"@id": ""}]
    "dct:source": [], // [{"@id": ""}]
    "dct:identifier": [], // [""]
    "adms:identifier": [], // [{"@id": "", "skos:notation": {"@type": "", "@value": ""}}]
    "foaf:page": [], // [{"@id": "", "@type": "foaf:Document", "dct:description": "", "dct:format": {"@id": ""}, "dct:title": ""}]
    "dcat:landingPage": [], // [{"@id": ""}]
    "dct:provenance": [], // [{"@id": "dct:ProvenanceStatement", "rdfs:label": ""}]
    "dct:accrualPeriodicity": {}, // {"@id": ""}
    "dct:accessRights": {}, // {"@id": ""}
    "dct:conformsTo": [], // [{"@id": "", "@type": "dct:Standard", "rdfs:label": ""}]
    "dct:relation": [], // [{"@id": ""}]
    "dcat:qualifiedRelation": [], // [{"@id": ""}]
    "prov:qualifiedAttribution": [], // [{"@id": ""}]
    "dct:spatial": {}, // {"@id": ""}
    "dcat:spatialResolutionInMeters": "", // ""
    "dct:temporal": [], // [{"@type": "dct:PeriodOfTime", "dcat:endDate": "", "dcat:startDate": ""}]
    "dcat:temporalResolution": "", // ""
    "dct:isReferencedBy": [], // [{"@id": ""}]
    "prov:wasGeneratedBy": [], // [{"@id": ""}]
    "dct:isVersionOf": [], // [{"@id": ""}]
    "dext:metadataExtension": [], // [{"@type": "dext:MetadataExtension", "dext:isUsedBy": {"@id": ""}}]
    "dct:hasVersion": [], // [{"@id": ""}]
    "owl:versionInfo": "", // ""
    "adms:versionNotes": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }]
};

const distributions = {
    "@id": "",
    "@type": "dcat:Distribution",
    "dcat:accessURL": [], // [{"@id": ""}]
    "dct:title": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }]
    "dct:description": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }]
    "dcat:accessService": [{"dct:title": [{"@value": "", "@language": "en"}], "dct:description": [{"@value": "", "@language": "en"}]}], // [{"@type": 'dcat:DataService', "dct:title": [{"@value": "", "@language": ""}], "dct:description": [{"@value": "", "@language": ""}], "dcat:endpointURL": {"@id": ""}}]
    "dcat:downloadURL": [], // [{"@id": ""}]
    "dct:format": {}, // {"@id": ""},
    "dct:license": {}, // {"@id": ""} OR {"@type": "dct:LicenseDocument", "dct:title": "", "skos:exactMatch": {"@id": ""}, "skos:prefLabel": ""}
    "dct:issued": "", // ""
    "dct:modified": "", // ""
    "dct:type": {}, // {"@id": ""}
    "dcat:mediaType": {}, // {"@id": ""}
    "dcat:availability": {}, // {"@id": ""}
    "dcat:byteSize": "", // ""
    "spdx:checksum": {}, // {"@type": "spdx:Checksum", "spdx:algorithm": {"@id": ""}, "spdx:checksumValue": ""}
    "dcat:compressFormat": {}, // {"@id": ""}
    "dcat:packageFormat": {}, // {"@id": ""}
    "dct:language": [], // [{"@id": ""}]
    "adms:status": {}, // {"@id": ""}
    "foaf:page": [], // [{"@id": "", "@type": "foaf:Document", "dct:description": "", "dct:format": {"@id": ""}, "dct:title": ""}]
    "dct:rights": {}, // {"@type": "dct:RightsStatement", "rdfs:label": {"@id": ""}} OR {"@type": "dct:RightsStatement", "rdfs:label": ""}
    "dct:conformsTo": [], // [{"@id": "", "@type": "dct:Standard", "rdfs:label": ""}]
    "odrl:hasPolicy": [], // [{"@id": ""}]
    "dcat:temporalResolution": "", // ""
    "dcat:spatialResolutionInMeters": "", // ""
};

const catalogues = {
    '@id': '',
    '@type': 'dcat:Catalogue',
    "dct:title": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }] 
    "dct:description": [{ "@value": "", "@language": "en" }], // [{ "@value": "", "@language": "" }]
    "dct:publisher": {}, // {"@id": ""} 
    "dct:license": {}, // {"@id": ""} OR {"@type": "dct:LicenseDocument", "dct:title": "", "skos:exactMatch": {"@id": ""}, "skos:prefLabel": ""}
    "foaf:homepage": {}, // {'@id': ""}
    "dcat:catalog": [], // [{'@id': ''}]
    "dct:language": [], // [{"@id": ""}]
    "dct:creator": [], // [{"@type": "", "foaf:homepage": {"@id": ""}, "foaf:mbox": {"@id": ""}, "fofa:name": ""}]
    "dct:spatial": {}, // {"@id": ""}
    "dct:rights": {}, // {"@type": "dct:RightsStatement", "rdfs:label": {"@id": ""}} OR {"@type": "dct:RightsStatement", "rdfs:label": ""}
    "dct:hasPart": [], // [{'@id': ''}]
    "dct:isPartOf": {}, // {'@id': ''}
};

export default { datasets, distributions, catalogues };
