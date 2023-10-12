<template>
  <div class="col-12">
    <div class="mb-3" v-if="showDatasetsOverview">

      <!-- DATASET ID && CATALOG -->
      <div class="mt-5 mb-0" >
        <div class="row">

          <!-- DATASET ID -->
          <div class="col-5 offset-1" v-if="showProperty('datasets', '@id')">
            <p class="mb-0">Dataset ID: {{ getString('datasets', '@id') }}</p>
          </div>

          <!-- CATALOG -->
          <div class="col-5 offset-1" v-if="showProperty('datasets', 'dct:catalog')">
            <p class="mb-0">Catalog: {{ getString('datasets', 'dct:catalog') }}</p>
          </div>
        </div>
        <hr>
      </div>

      <!-- TITLE -->
      <div class="mt-2 mb-4" v-if="showProperty('datasets', 'dct:title')">
        <div class="row">
          <div class="col-8 offset-1">
            <h2 v-for="(title, index) in getLanguageArray('datasets', 'dct:title')" :key="index">
              {{ languageNames[title['@language']] }}: {{ title['@value'] }}
            </h2>
          </div>
        </div>
      </div>

      <!-- DESCRIPTION -->
      <div class="mt-2" v-if="showProperty('datasets', 'dct:description')">
        <div class="row">
          <div class="col-10 offset-1">
            <p v-for="(description, index) in getLanguageArray('datasets', 'dct:description')" :key="index">
              {{ languageNames[description['@language']] }}: {{ description['@value'] }}
            </p>
          </div>
        </div>
        <hr>
      </div>

      <!-- DISTRIBUTIONS -->
      <div class="mt-2">
        <div class="row">
          <div class="col-12">
            <div class="row">
              <div class="col-10 offset-1 py-2 text-left">
                <h2>{{ $t('message.metadata.distributions') }} ({{ values.distributions.length }})</h2>
              </div>
              <ul class="list list-unstyled col-12" v-if="showDistributions">
                <li class="row" v-for="(distribution, i) in getData('distributions')" :key="`distribution${i+1}`">
                  <span class="d-inline-block col-md-1 col-2 pt-3 pr-md-1 pr-0 m-md-0 m-auto">
                    <div v-if="showProperty(`distribution_${i+1}`, 'dct:format')" class="circle float-md-right text-center text-white text-truncate"
                         :type="getDistributionFormat(distribution)"
                         :title="getDistributionFormat(distribution)">
                      <span>
                        {{ truncate(getDistributionFormat(distribution), 4, true) }}
                      </span>
                    </div>
                    <div v-else class="circle float-md-right text-center text-white text-truncate" type="UNKNOWN" title="UNKNOWN"><span>UNKNOWN</span></div>
                  </span>
                  <span class="col-10">
                    <span class="row">
                      <span class="d-inline-block col-md-7 col-12">
                        <span v-if="showProperty(`distribution_${i+1}`, 'dct:title')">
                          <h6 class="m-0" v-for="(title, index) in getLanguageArray(`distribution_${i+1}`, 'dct:title')" :key="index">
                            {{ languageNames[title['@language']] }}: {{ title['@value'] }}
                          </h6>
                        </span>
                        <span class="mt-2 d-block text-muted text-truncate" v-if="showProperty(`distribution_${i+1}`, 'dct:description')">
                          <small v-for="(description, index) in getLanguageArray(`distribution_${i+1}`, 'dct:description')" :key="index">
                            {{ languageNames[description['@language']] }}: {{ description['@value'] }}
                          </small>
                        </span>
                        <span class="mt-2 d-block"  v-if="showProperty(`distribution_${i+1}`, 'dct:license')">
                          <small class="font-weight-bold">
                            {{ $t('message.metadata.license') }} : {{ getString(`distribution_${i+1}`, 'dct:license') }}
                          </small>
                        </span>
                      </span>
                      <span class="col-md-5 col-12 mt-2 text-md-right text-left" v-if="showProperty(`distribution_${i+1}`, 'dct:issued')">
                        <span class="d-inline-block">
                          <small class="pr-1">{{ filterDateFormatEU(getString(`distribution_${i+1}`, 'dct:issued')) }}</small>
                        </span>
                      </span>
                    </span>
                    <hr>
                  </span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <!-- KEYWORDS -->
      <div class="mt-2" v-if="showLanguageArray('datasets', 'dcat:keyword')">
        <div class="row">
          <div class="col-10 offset-1">
            <div class="row">
              <span class="col-4 col-sm-3 col-md-2 mt-md-0 mt-3 pr-0" v-for="(keyword, index) in getLanguageArray('datasets', 'dcat:keyword')" :key="index">
                <small class="d-inline-block w-100 p-2 ml-1 rounded-pill text-center text-white text-truncate bg-primary" :title="keyword['@value']">
                  {{ languageNames[keyword['@language']] }}: {{ keyword['@value'] }}
                </small>
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- INFO TABLE -->
      <div class="mt-5" v-if="showTable">
        <div class="row">
          <div class="col-10 offset-1 py-2 bg-white">
            <h2 class="heading">{{ $t('message.datasetDetails.additionalInfo') }}</h2>
          </div>
          <div class="col-10 offset-1">
            <table class="table table-borderless table-responsive pl-3 bg-light">

              <!-- PUBLISHER -->
              <tr v-if="showProperty('datasets', 'dct:publisher')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.publisher') }}</td>
                <td>{{ getString('datasets', 'dct:publisher') }}</td>
              </tr>

              <!-- CONTACT POINT -->
              <tr v-if="showProperty('datasets', 'dcat:contactPoint')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.contactPoints') }}</td>
                <td v-if="showObjectArray('datasets', 'dcat:contactPoint')">
                  <div v-for="(contactPoint, index) in getObjectArray('datasets', 'dcat:contactPoint')" :key="index">
                    <div v-if="showValue(contactPoint, '@type')">
                      {{ $t('message.metadata.type') }}: {{ contactPoint['@type'] }}
                    </div>
                    <div v-if="showValue(contactPoint, 'vcard:fn')">
                      {{ $t('message.metadata.name') }}: {{ contactPoint['vcard:fn'] }}
                    </div>
                    <div v-if="showValue(contactPoint, 'vcard:hasEmail')">
                      {{ $t('message.metadata.email') }}: <app-link :to="`mailto:${contactPoint['vcard:hasEmail']}`">{{ contactPoint['vcard:hasEmail']}}</app-link>
                    </div>
                    <div v-if="showValue(contactPoint, 'vcard:hasOrganizationName')">
                      {{ $t('message.metadata.organizationName') }}: {{ contactPoint['vcard:hasOrganizationName'] }}
                    </div>
                    <div v-if="showValue(contactPoint, 'vcard:hasTelephone')">
                      {{ $t('message.metadata.telephone') }}: {{ contactPoint['vcard:hasTelephone'] }}
                    </div>
                    <div v-if="showValue(contactPoint, 'vcard:hasURL')">
                      {{ $t('message.metadata.url') }}: <app-link :to="contactPoint['vcard:hasURL']">{{ contactPoint['vcard:hasURL']}}</app-link>
                    </div>
                    <div v-if="showValue(contactPoint, 'vcard:hasAddress')">
                      {{ $t('message.metadata.address') }}: {{ contactPoint['vcard:hasAddress'][0]['vcard:street_address'] }},
                                                            {{ contactPoint['vcard:hasAddress'][0]['vcard:postal_code'] }}
                                                            {{ contactPoint['vcard:hasAddress'][0]['vcard:locality'] }},
                                                            {{ contactPoint['vcard:hasAddress'][0]['vcard:country_name'] }}
                    </div>
                    <br>
                  </div>
                </td>
              </tr>

              <!-- CREATOR -->
              <tr v-if="showProperty('datasets', 'dct:creator')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.creator') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:creator')">
                  <div v-for="(creator, index) in getObjectArray('datasets', 'dct:creator')" :key="index">
                    <div v-if="showValue(creator, '@type')">
                      {{ $t('message.metadata.type') }}: {{ creator['@type'] }}
                    </div>
                    <div v-if="showValue(creator, 'foaf:name')">
                      {{ $t('message.metadata.name') }}: {{ creator['foaf:name'] }}
                    </div>
                    <div v-if="showValue(creator, 'foaf:mbox')">
                      {{ $t('message.metadata.email') }}: <app-link :to="`mailto:${creator['foaf:mbox']}`">{{ creator['foaf:mbox'] }}</app-link>
                    </div>
                    <div v-if="showValue(creator, 'foaf:homepage')">
                      {{ $t('message.metadata.homepage') }}: <app-link :to="creator['foaf:homepage']">{{ creator['foaf:homepage'] }}</app-link>
                    </div>
                  </div>
                </td>
              </tr>

              <!-- ISSUED -->
              <tr v-if="showProperty('datasets', 'dct:issued')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.created') }} </td>
                <td>{{ filterDateFormatEU(getString('datasets', 'dct:issued')) }}</td>
              </tr>

              <!-- MODIFIED -->
              <tr v-if="showProperty('datasets', 'dct:modified')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.updated') }}</td>
                <td>{{ filterDateFormatEU(getString('datasets', 'dct:modified')) }}</td>
              </tr>

              <!-- LANGUAGES -->
              <tr v-if="showProperty('datasets', 'dct:language')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.languages') }}</td>
                <td v-if="showStringArray('datasets', 'dct:language')">
                  <div v-for="(language, index) in getStringArray('datasets', 'dct:language')" :key="index">
                    <div v-if="showString(language)">
                      {{ language }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- SUBJECT -->
              <tr v-if="showProperty('datasets', 'dct:subject')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.subject.label') }}</td>
                <td v-if="showStringArray('datasets', 'dct:subject')">
                  <div v-for="(subject, index) in getStringArray('datasets', 'dct:subject')" :key="index">
                    <div v-if="showString(subject)">
                      {{ subject }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- THEME -->
              <tr v-if="showProperty('datasets', 'dcat:theme')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.theme.label') }}</td>
                <td v-if="showStringArray('datasets', 'dcat:theme')">
                  <div v-for="(theme, index) in getStringArray('datasets', 'dcat:theme')" :key="index">
                    <div v-if="showString(theme)">
                      {{ theme }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- TYPE -->
              <tr v-if="showProperty('datasets', 'dct:type')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.type') }}</td>
                <td v-if="showStringArray('datasets', 'dct:type')">
                  <div v-for="(type, index) in getStringArray('datasets', 'dct:type')" :key="index">
                    <div v-if="showString(type)">
                      {{ type }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- SOURCE -->
              <tr v-if="showProperty('datasets', 'dct:source')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.sources') }}</td>
                <td v-if="showStringArray('datasets', 'dct:source')">
                  <div v-for="(source, index) in getObjectArray('datasets', 'dct:source')" :key="index">
                    <div v-if="showValue(source, '@value')">
                      {{ source['@value'] }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- IDENTIFIER -->
              <tr v-if="showProperty('datasets', 'dct:identifier')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.identifiers') }}</td>
                <td v-if="showStringArray('datasets', 'dct:identifier')">
                  <div v-for="(identifier, index) in getObjectArray('datasets', 'dct:identifier')" :key="index">
                    <div v-if="showValue(identifier, '@value')">
                      {{ identifier['@value'] }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- ADMS:IDENTIFIER -->
              <tr v-if="showProperty('datasets', 'adms:identifier')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.otherIdentifiers') }}</td>
                <td v-if="showObjectArray('datasets', 'adms:identifier')">
                  <div v-for="(identifier, index) in getObjectArray('datasets', 'adms:identifier')" :key="index">
                    <div v-if="showValue(identifier, '@id')">
                      {{ $t('message.metadata.url') }}: {{ identifier['@id'] }}
                    </div>
                    <div v-if="showValue(identifier['skos:notation'][0], '@value')">
                      {{ $t('message.metadata.identifier') }}: {{ identifier['skos:notation'][0]['@value'] }}
                    </div>
                    <div v-if="showValue(identifier['skos:notation'][0], '@type')">
                      {{ $t('message.metadata.type') }}: {{ identifier['skos:notation'][0]['@type'] }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- PAGE -->
              <tr v-if="showProperty('datasets', 'foaf:page')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.page.label') }}</td>
                <td v-if="showObjectArray('datasets', 'foaf:page')">
                  <div v-for="(page, index) in getObjectArray('datasets', 'foaf:page')" :key="index">
                    <div v-if="showValue(page, 'dct:title')">
                      {{ $t('message.metadata.title') }}: {{ page['dct:title'] }}
                    </div>
                    <div v-if="showValue(page, 'dct:description')">
                      {{ $t('message.metadata.description') }}: {{ page['dct:description'] }}
                    </div>
                    <div v-if="showValue(page, 'dct:format')">
                      {{ $t('message.metadata.format') }}:{{ page['dct:format'] }}
                    </div>
                    <div v-if="showValue(page, '@id')">
                      {{ $t('message.metadata.url') }}: <app-link :to="page['@id']">{{ page['@id'] }}</app-link>
                    </div>
                  </div>
                </td>
              </tr>

              <!-- LANDING PAGE -->
              <tr v-if="showProperty('datasets', 'dcat:landingPage')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.landingPage') }}</td>
                <td v-if="showObjectArray('datasets', 'dcat:landingPage')">
                  <div v-for="(landingPage, index) in getObjectArray('datasets', 'dcat:landingPage')" :key="index">
                    <app-link v-if="showValue(landingPage, '@value')" :to="landingPage">{{ landingPage['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- PROVENANCE -->
              <tr v-if="showProperty('datasets', 'dct:provenance')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.provenances') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:provenance')">
                  <div v-for="(provenance, index) in getObjectArray('datasets', 'dct:provenance')" :key="index">
                    <div v-if="showValue(provenance, 'rdfs:label')">
                      {{ provenance['rdfs:label'] }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- ACCRUAL PERIODICITY -->
              <tr v-if="showProperty('datasets', 'dct:accrualPeriodicity')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.accrualPeriodicity') }}</td>
                <td>{{ getString('datasets', 'dct:accrualPeriodicity') }}</td>
              </tr>

              <!-- ACCESS RIGHTS -->
              <tr v-if="showProperty('datasets', 'dct:accessRights')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.accessRights') }}</td>
                <td>{{ getString('datasets', 'dct:accessRights') }}</td>
              </tr>

              <!-- CONFORMS TO -->
              <tr v-if="showProperty('datasets', 'dct:conformsTo')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.conformsTo') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:conformsTo')">
                  <div v-for="(conformsTo, index) in getObjectArray('datasets', 'dct:conformsTo')" :key="index">
                    <div v-if="showValue(conformsTo, 'rdfs:label')">
                      {{ conformsTo['rdfs:label'] }}
                    </div>
                    <div v-if="showValue(conformsTo, '@id')">
                      {{ conformsTo['@id'] }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- RELATION -->
              <tr v-if="showProperty('datasets', 'dct:relation')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.relation.label') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:relation')">
                  <div v-for="(relation, index) in getObjectArray('datasets', 'dct:relation')" :key="index">
                    <app-link v-if="showValue(relation, '@value')" :to="relation">{{ relation['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- QUALIFIED RELATION -->
              <tr v-if="showProperty('datasets', 'dcat:qualifiedRelation')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.qualifiedRelation.label') }}</td>
                <td v-if="showObjectArray('datasets', 'dcat:qualifiedRelation')">
                  <div v-for="(qualifiedRelation, index) in getObjectArray('datasets', 'dcat:qualifiedRelation')" :key="index">
                    <app-link v-if="showValue(qualifiedRelation, '@value')" :to="qualifiedRelation">{{ qualifiedRelation['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- QUALIFIED ATTRIBUTION -->
              <tr v-if="showProperty('datasets', 'prov:qualifiedAttribution')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.qualifiedAttribution.label') }}</td>
                <td v-if="showObjectArray('datasets', 'prov:qualifiedAttribution')">
                  <div v-for="(qualifiedAttribution, index) in getObjectArray('datasets', 'prov:qualifiedAttribution')" :key="index">
                    <app-link v-if="showValue(qualifiedAttribution, '@value')" :to="qualifiedAttribution">{{ qualifiedAttribution['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- SPATIAL -->
              <tr v-if="showProperty('datasets', 'dct:spatial')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.spatial') }}</td>
                <td>{{ getString('datasets', 'dct:spatial') }}</td>
              </tr>

              <!-- SPATIAL RESOLUTION IN METERS -->
              <tr v-if="showProperty('datasets', 'dcat:spatialResolutionInMeters')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.spatialResolutionInMeters.label') }}</td>
                <td>{{ getString('datasets', 'dcat:spatialResolutionInMeters') }}</td>
              </tr>

              <!-- TEMPORAL -->
              <tr v-if="showProperty('datasets', 'dct:temporal')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.temporal') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:temporal')">
                  <div v-for="(temporal, index) in getObjectArray('datasets', 'dct:temporal')" :key="index">
                    <div v-if="showValue(temporal, 'dcat:startDate')">
                      {{ temporal['dcat:startDate'] }}
                    </div>
                    <div v-if="showValue(temporal, 'dcat:endDate')">
                      {{ temporal['dcat:endDate'] }}
                    </div>
                  </div>
                </td>
              </tr>

              <!-- TEMPORAL RESOLUTION -->
              <tr v-if="showProperty('datasets', 'dcat:temporalResolution')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.temporalResolution.label') }}</td>
                <td v-if="showObjectArray('datasets', 'dcat:temporalResolution')">
                  <div v-for="(temporalResolution, index) in getObjectArray('datasets', 'dcat:temporalResolution')" :key="index">
                    <div>
                      <!-- {{ $t('message.metadata.date') }}: -->
                      Date:
                      <span v-if="showValue(temporalResolution, 'Day')">
                         {{ addPrecedingZero(temporalResolution['Day']) }}{{ temporalResolution['Day'] }}
                      </span>
                      .
                      <span v-if="showValue(temporalResolution, 'Month')">
                         {{ addPrecedingZero(temporalResolution['Month']) }}{{ temporalResolution['Month'] }}
                      </span>
                      .
                      <span v-if="showValue(temporalResolution, 'Year')">
                        {{ temporalResolution['Year'] }}
                      </span>
                    </div>
                    <div>
                      <!-- {{ $t('message.metadata.time') }}: -->
                      Time:
                      <span v-if="showValue(temporalResolution, 'Hour')">
                         {{ addPrecedingZero(temporalResolution['Hour']) }}{{ temporalResolution['Hour'] }}
                      </span>
                      :
                      <span v-if="showValue(temporalResolution, 'Minute')">
                        {{ addPrecedingZero(temporalResolution['Minute']) }}{{ temporalResolution['Minute'] }}
                      </span>
                      :
                      <span v-if="showValue(temporalResolution, 'Second')">
                        {{ addPrecedingZero(temporalResolution['Second']) }}{{ temporalResolution['Second'] }}
                      </span>
                    </div>
                  </div>
                </td>
              </tr>

              <!-- IS REFERENCED BY -->
              <tr v-if="showProperty('datasets', 'dct:isReferencedBy')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.isReferencedBy.label') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:isReferencedBy')">
                  <div v-for="(isReferencedBy, index) in getObjectArray('datasets', 'dct:isReferencedBy')" :key="index">
                    <app-link v-if="showValue(isReferencedBy, '@value')" :to="isReferencedBy">{{ isReferencedBy['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- WAS GENERATED BY -->
              <tr v-if="showProperty('datasets', 'prov:wasGeneratedBy')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.wasGeneratedBy.label') }}</td>
                <td v-if="showObjectArray('datasets', 'prov:wasGeneratedBy')">
                  <div v-for="(wasGeneratedBy, index) in getObjectArray('datasets', 'prov:wasGeneratedBy')" :key="index">
                    <app-link v-if="showValue(wasGeneratedBy, '@value')" :to="wasGeneratedBy">{{ wasGeneratedBy['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- IS VERSION OF -->
              <tr v-if="showProperty('datasets', 'dct:isVersionOf')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.isVersionOf') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:isVersionOf')">
                  <div v-for="(isVersionOf, index) in getObjectArray('datasets', 'dct:isVersionOf')" :key="index">
                    <app-link v-if="showValue(isVersionOf, '@value')" :to="isVersionOf">{{ isVersionOf['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- IS USED BY -->
              <tr v-if="showProperty('datasets', 'dext:metadataExtension')">
                <td class="w-25 font-weight-bold">{{ $t('message.dataupload.datasets.step2.isUsedBy.label') }}</td>
                <td v-if="showObjectArray('datasets', 'dext:metadataExtension')">
                  <div v-for="(isVersionOf, index) in getObjectArray('datasets', 'dext:metadataExtension')" :key="index">
                    <app-link v-if="showValue(isVersionOf, 'dext:isUsedBy')" :to="isVersionOf">{{ isVersionOf['dext:isUsedBy'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- HAS VERSION -->
              <tr v-if="showProperty('datasets', 'dct:hasVersion')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.hasVersion') }}</td>
                <td v-if="showObjectArray('datasets', 'dct:hasVersion')">
                  <div v-for="(hasVersion, index) in getObjectArray('datasets', 'dct:hasVersion')" :key="index">
                    <app-link v-if="showValue(hasVersion, '@value')" :to="hasVersion">{{ hasVersion['@value'] }}</app-link>
                  </div>
                </td>
              </tr>

              <!-- VERSION INFO -->
              <tr v-if="showProperty('datasets', 'owl:versionInfo')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.versionInfo') }}</td>
                <td>{{ getString('datasets', 'owl:versionInfo') }}</td>
              </tr>

              <!-- VERSION NOTES -->
              <tr v-if="showLanguageArray('datasets', 'adms:versionNotes')">
                <td class="w-25 font-weight-bold">{{ $t('message.metadata.versionNotes') }}</td>
                <td v-if="showLanguageArray('datasets', 'adms:versionNotes')">
                  <div v-for="(versionNotes, index) in getLanguageArray('datasets', 'adms:versionNotes')" :key="index">
                    {{ languageNames[versionNotes['@language']] }}: {{ versionNotes['@value'] }}
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div class="mb-3" v-if="showCatalogsOverview">
    </div>
  </div>
</template>

<script>
/* eslint-disable no-restricted-syntax,guard-for-in */
import axios from 'axios';
import { mapActions, mapGetters } from 'vuex';
import {
  has,
  isNil,
  isEmpty,
  isString,
  isArray,
  isObject,
} from 'lodash';
import LANGS from '../../../../config/data-provider-interface/selector-languages.json';
import { AppLink, helpers, dateFilters } from "@piveau/piveau-hub-ui-modules";
const { getTranslationFor, truncate } = helpers;

export default {
  components: {
    appLink: AppLink,
  },
  props: ['property'],
  data() {
    return {
      values: {},
      languageNames: LANGS,
    };
  },
  computed: {
    ...mapGetters('auth', [
      'getIsEditMode',
    ]),
    ...mapGetters('dataProviderInterface', [
      'getNumberOfDistributions',
      'getNavSteps',
      'getData',
    ]),
    showDatasetsOverview() {
      return this.$route.params.property === 'datasets' && has(this.values, 'datasets');
    },
    showCatalogsOverview() {
      return this.$route.params.property === 'catalogues' && has(this.values, 'catalogues');
    },
    showDistributions() {
      return this.values.distributions.length > 0;
    },
    showTable() {
      /* eslint-disable semi-style */
      return this.showProperty('datasets', 'dct:publisher')
          || this.showProperty('datasets', 'dcat:contactPoint')
          || this.showProperty('datasets', 'dct:creator')
          || this.showProperty('datasets', 'dct:issued')
          || this.showProperty('datasets', 'dct:modified')
          || this.showProperty('datasets', 'dct:language')
          || this.showProperty('datasets', 'dct:subject')
          || this.showProperty('datasets', 'dcat:theme')
          || this.showProperty('datasets', 'dct:type')
          || this.showProperty('datasets', 'dct:source')
          || this.showProperty('datasets', 'dct:identifier')
          || this.showProperty('datasets', 'adms:identifier')
          || this.showProperty('datasets', 'foaf:page')
          || this.showProperty('datasets', 'dcat:landingPage')
          || this.showProperty('datasets', 'dct:provenance')
          || this.showProperty('datasets', 'dct:accrualPeriodicity')
          || this.showProperty('datasets', 'dct:accessRights')
          || this.showProperty('datasets', 'dct:conformsTo')
          || this.showProperty('datasets', 'dct:relation')
          || this.showProperty('datasets', 'dcat:qualifiedRelation')
          || this.showProperty('datasets', 'prov:qualifiedAttribution')
          || this.showProperty('datasets', 'dct:spatial')
          || this.showProperty('datasets', 'dcat:spatialResolutionInMeters')
          || this.showProperty('datasets', 'dct:temporal')
          || this.showProperty('datasets', 'dcat:temporalResolution')
          || this.showProperty('datasets', 'dct:isReferencedBy')
          || this.showProperty('datasets', 'prov:wasGeneratedBy')
          || this.showProperty('datasets', 'dct:isVersionOf')
          || this.showProperty('datasets', 'dext:isUsedBy')
          || this.showProperty('datasets', 'dct:hasVersion')
          || this.showProperty('datasets', 'owl:versionInfo')
          || this.showProperty('datasets', 'adms:versionNotes')
      ;
    },
  },
  methods: {
    ...mapActions('dataProviderInterface', [
      'saveExistingJsonld',
    ]),
    has,
    isNil,
    isEmpty,
    isString,
    isArray,
    isObject,
    getTranslationFor,
    truncate,
    capitalize(word) {
      return `${word.substring(0, 1).toUpperCase()}${word.substring(1)}`;
    },
    filterDateFormatEU(date) {
      return dateFilters.formatEU(date);
    },
    showValue(property, value) {
      return has(property, value) && !isNil(property[value]);
    },
    showString(property) {
      return isString(property) && !isNil(property);
    },
    showProperty(property, name) {
      return has(this.values, property) && has(this.values[property], name) && !isNil(this.values[property][name]);
    },
    showStringArray(property, name) {
      return this.showProperty(property, name) && isArray(this.values[property][name]);
    },
    showObjectArray(property, name) {
      return this.showProperty(property, name) && isArray(this.values[property][name]);
    },
    showLanguageArray(property, name) {
      return this.showObjectArray(property, name) && this.values[property][name].filter(el => has(el, '@value') && has(el, '@language')).length > 0;
    },
    getString(property, name) {
      return this.values[property][name];
    },
    getStringArray(property, name) {
      return this.values[property][name];
    },
    getObjectArray(property, name) {
      return this.values[property][name];
    },
    getLanguageArray(property, name) {
      return this.values[property][name].filter(el => has(el, '@value') && has(el, '@language'));
    },
    getDistributionFormat(distribution) {
      return distribution['dct:format'].substring(distribution['dct:format'].lastIndexOf('/') + 1);
    },
    getLocalstorageValues() {
      this.values[this.property] = this.getData(this.property);
      if (this.property === 'datasets') {
        this.values['distributions'] = this.getData('distributions');
      }
    },
    checkDatasetMandatory() {
      // Check if mandatory dataset properties are set
      if (!this.showProperty('datasets', 'dct:title') || !this.showProperty('datasets', 'dct:description') || !this.showProperty('datasets', 'dct:catalog')) {
        this.$router.push({ name: 'DataProviderInterface-Input', params: { property: 'datasets', page: 'step1' }, query: { error: 'mandatory' } });
      }
    },
    checkDistributionMandatory() {
      // Check if mandatory distribution properties are set
      if (Object.keys(this.values).filter(el => el.startsWith('distribution')).filter(dist => !has(this.values[dist], 'dcat:accessURL')).length > 0) {
        this.$router.push({
          name: 'DataProviderInterface-ID',
          path: '/dpi/datasets/step3/distribution1',
          params: {
            property: 'datasets',
            page: 'step3',
            id: 'distribution1',
          },
          query: { 
            locale: this.$route.query.locale,
            error: 'mandatory',
          },
        });
      }
    },
    checkCatalogueMandatory() {
      // Check if mandatory catalogue properties are set
      if (!this.showProperty('catalogues', 'dct:title') || !this.showProperty('datasets', 'dct:description')) {
        this.$emit('error');
      }
    },
    checkDatasetID() {
      // Check uniqueness of Dataset ID
      if (!this.getIsEditMode) {
        this.checkUniqueID()
          .then((isUniqueID) => {
            if (!isUniqueID) {
              // Dataset ID not unique / taken in meantime --> Redirect to step1 where the user can choose a new ID
              this.$router.push({ name: 'DataProviderInterface-Input', params: { property: 'datasets', page: 'step1' }, query: { error: 'id' } });
            }
          });
      }
    },
    checkUniqueID() {
      return new Promise((resolve) => {
        if (this.values.datasets.datasetID !== '') {
          const request = `${this.$env.api.hubUrl}datasets/${this.values.datasets.datasetID}?useNormalizedId=true`;
          axios.head(request)
            .then(() => {
              resolve(false);
            })
            .catch(() => {
              resolve(true);
            });
        }
      });
    },
    cleanupDistributions() {
      // Remove empty distributions
      let numberOfDistributions = localStorage.getItem('numberOfDistributions');
      Object.keys(this.values).filter(el => el.startsWith('distribution')).forEach((distEl) => {
        if (isEmpty(this.values[distEl])) {
          numberOfDistributions -= 1;
          delete this.values[distEl];
        }
      });

      // Set new numberOfDistributions
      localStorage.setItem('numberOfDistributions', JSON.stringify(numberOfDistributions));
    },
    addPrecedingZero(value) {
      return parseInt(value, 10) < 10 ? 0 : '';
    },
  },
  created() {
    this.getLocalstorageValues();
    // this.$nextTick(() => {
    //   if (this.property === 'datasets') {
    //     this.checkDatasetMandatory();
    //     this.checkDatasetID();
    //     this.cleanupDistributions();
    //     this.checkDistributionMandatory();
    //   }

    //   if (this.property === 'catalogues') {
    //     this.checkCatalogueMandatory();
    //   }
    // });
  },
  mounted() {
    this.saveExistingJsonld(this.property);
  }
};
</script>

<style lang="scss" scoped>
  .heading, .description, .arrow {
    cursor: pointer;
  }

  .options, .download {
    .dropdown-menu {
      min-width: 300px;
      .dropdown-item {
        &:hover {
          color: initial;
          background-color: initial;
        }
      }
    }
  }

  .circle {
    width: 40px;
    height: 40px;
    margin: 0 auto;
    padding: 20px 0;
    font-size: 12px;
    line-height: 1px;
    border-radius: 50%;
    background-color: #000000;
    &[type="HTML"] {
      background-color: #285C76;
    }
    &[type="JSON"] {
      background-color: var(--dark-orange);
    }
    &[type="XML"] {
      background-color: #8F4300;
    }
    &[type="TXT"] {
      background-color: #2B5E73;
    }
    &[type="CSV"] {
      background-color: var(--badge-green);
    }
    &[type="XLS"] {
      background-color: #1A6537;
    }
    &[type="ZIP"] {
      background-color: #252525;
    }
    &[type="API"] {
      background-color: #923560;
    }
    &[type="PDF"] {
      background-color: #B30519;
    }
    &[type="SHP"] {
      background-color: var(--badge-black);
    }
    &[type="RDF"],
    &[type="NQUAD"],
    &[type="NTRIPLES"],
    &[type="TURTLE"] {
      background-color: #0b4498;
    }
  }
</style>
