<template>
  <div class="ecl-container">
   <div class="ecl-row ecl-u-mt-l">
      <div class="ecl-col-l-3 pl-0">
         <nav data-ecl-auto-init="InpageNavigation" class="ecl-inpage-navigation" data-ecl-inpage-navigation="true">
            <div class="ecl-inpage-navigation__title">Page contents</div>
            <div class="ecl-inpage-navigation__body">
               <button type="button" class="ecl-inpage-navigation__trigger"
                  id="ecl-inpage-navigation-trigger" data-ecl-inpage-navigation-trigger="true"
                  aria-controls="ecl-inpage-navigation-list" aria-expanded="false">
                  <span
                     class="ecl-inpage-navigation__trigger-current"
                     data-ecl-inpage-navigation-trigger-current="true">&nbsp;</span>
                  <svg focusable="false" aria-hidden="true"
                     class="ecl-inpage-navigation__trigger-icon ecl-icon ecl-icon--s ecl-icon--rotate-180">
                     <use xlink:href="/icons.svg#corner-arrow"></use>
                  </svg>
               </button>
               <ul class="ecl-inpage-navigation__list" aria-labelledby="ecl-inpage-navigation-trigger"
                  data-ecl-inpage-navigation-list="true" id="ecl-inpage-navigation-list">
                  <li class="ecl-inpage-navigation__item"> <a data-ecl-inpage-navigation-link="true" href="#inline-nav-1"
                     class="ecl-inpage-navigation__link ecl-link ecl-link--standalone">{{ $t("message.methodology.scope.headline") }}</a> </li>
                  <li class="ecl-inpage-navigation__item"> <a data-ecl-inpage-navigation-link="true" href="#inline-nav-2"
                     class="ecl-inpage-navigation__link ecl-link ecl-link--standalone">{{ $t("message.methodology.cover.headline") }}</a> </li>
                  <li class="ecl-inpage-navigation__item"> <a data-ecl-inpage-navigation-link="true" href="#inline-nav-3"
                     class="ecl-inpage-navigation__link ecl-link ecl-link--standalone">{{ $t("message.methodology.process.headline") }}</a> </li>
                  <li class="ecl-inpage-navigation__item"> <a data-ecl-inpage-navigation-link="true" href="#inline-nav-4"
                     class="ecl-inpage-navigation__link ecl-link ecl-link--standalone">{{ $t("message.methodology.assumptions.headline") }}</a> </li>
                  <li class="ecl-inpage-navigation__item"> <a data-ecl-inpage-navigation-link="true" href="#inline-nav-5"
                     class="ecl-inpage-navigation__link ecl-link ecl-link--standalone"> {{ $t("message.dashboard.dimensions") }}</a> </li>
                  <li class="ecl-inpage-navigation__item"> <a data-ecl-inpage-navigation-link="true" href="#inline-nav-6"
                     class="ecl-inpage-navigation__link ecl-link ecl-link--standalone">{{ $t("message.methodology.scoring.table.scoring_cat") }}</a> </li>
               </ul>
            </div>
         </nav>
      </div>
      <div class="ecl-col-l-9 px-0">
         <h3 class="ecl-u-type-heading-3 ecl-u-mb-m" id="inline-nav-1">{{ $t("message.methodology.scope.headline") }}</h3>
         <p class="ecl-u-type-paragraph-m m-0"> <span v-html="$t('message.methodology.scope.main_text')"></span> </p>
         <h3 class="ecl-u-type-heading-3 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-2">{{ $t("message.methodology.cover.headline") }} </h3>
         <p class="ecl-u-type-paragraph-m m-0">
            <span v-html="$t('message.methodology.cover.main_text')"></span>
         </p>
         <ul>
            <li>
               <app-link :to="shaclValidatorUrl" target="_blank" rel="noopener"> {{$t('message.methodology.cover.validator_ui')}} <i class=fas fa-extern-link-alt fa-sm></i></app-link>
            </li>
            <li>
               <app-link :to="shaclApiUrl" target="_blank" rel="noopener"> {{$t('message.methodology.cover.validator_service')}} <i class=fas fa-extern-link-alt fa-sm></i></app-link>
            </li>
         </ul>
         <h3 class="ecl-u-type-heading-3 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-3">{{ $t("message.methodology.process.headline") }}</h3>
         <p class="ecl-u-type-paragraph-m m-0"> <span v-html="$t('message.methodology.process.main_text')"></span> </p>
         <h3 class="ecl-u-type-heading-3 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-4">{{ $t("message.methodology.assumptions.headline") }}</h3>
         <p class="ecl-u-type-paragraph-m m-0"> <span v-html="$t('message.methodology.assumptions.main_text')"></span></p>
         <h3 class="ecl-u-type-heading-3 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-5">{{ $t("message.dashboard.dimensions") }}</h3>
         <p class="ecl-u-type-paragraph-m m-0"> <span v-html="$t('message.methodology.dimensions.main_text')"></span></p>
         <h4 class="ecl-u-type-heading-4 ecl-u-mb-m ecl-u-mt-l">{{ $t("message.dashboard.findability.title") }}</h4>
         <p class="ecl-u-type-paragraph-m m-0"> <span v-html="$t('message.methodology.dimensions.findability.main_text')"></span></p>
         <div class="doc-space"></div>
         <div class="table-responsive">
            <table class="ecl-table table table-hover">
               <thead class="ecl-table__head">
                  <tr class="ecl-table__row head-tb">
                     <th class="ecl-table__header" width="160px">{{ $t("message.methodology.dimensions.table.metric_name") }}</th>
                     <th class="ecl-table__header" width="260px">{{ $t("message.methodology.dimensions.table.desc") }}</th>
                     <th class="ecl-table__header" width="370px">{{ $t("message.methodology.dimensions.table.metric") }}</th>
                     <th class="ecl-table__header weight-row" width="60px">{{ $t("message.methodology.dimensions.table.weight") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <th width="156px" id="keywordAvailability">{{ $t("message.methodology.dimensions.findability.keywords_name_1") }}</th>
                     <td width="252px">{{ $t("message.methodology.dimensions.findability.keywords_desc_1") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.findability.keywords_metric_1") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dcat:keyword</span>
                        </div>
                     </td>
                     <td class="weight-row">30</td>
                  </tr>
                  <tr>
                     <th id="categoryAvailability">{{ $t("message.methodology.dimensions.findability.keywords_name_2") }}</th>
                     <td>{{ $t("message.methodology.dimensions.findability.keywords_desc_2") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.findability.keywords_metric_2") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dcat:theme</span>
                        </div>
                     </td>
                     <td class="weight-row">30</td>
                  </tr>
                  <tr>
                     <th id="spatialAvailability">{{ $t("message.methodology.dimensions.findability.keywords_name_3") }}</th>
                     <td>{{ $t("message.methodology.dimensions.findability.keywords_desc_3") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.findability.keywords_metric_3") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dct:spatial</span>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="temporalAvailability">{{ $t("message.methodology.dimensions.findability.keywords_name_4") }}</th>
                     <td>{{ $t("message.methodology.dimensions.findability.keywords_desc_4") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.findability.keywords_metric_4") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dct:temporal</span>
                        </div>
                     </td>
                     <td class="weight-row"> 20 </td>
                  </tr>
               </tbody>
            </table>
         </div>
         <h4 class="ecl-u-type-heading-4 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-5">{{ $t("message.dashboard.accessibility.title") }}</h4>
         <p class="ecl-u-type-paragraph-m m-0">   <span v-html="$t('message.methodology.dimensions.accessible.main_text')"></span></p>
         <div class="doc-space"></div>
         <div class="table-responsive">
            <table class="ecl-table table table-hover">
               <thead class="ecl-table__head">
                  <tr class="ecl-table__row head-tb">
                     <th class="ecl-table__header" width="160px">{{ $t("message.methodology.dimensions.table.metric_name") }}</th>
                     <th class="ecl-table__header" width="260px">{{ $t("message.methodology.dimensions.table.desc") }}</th>
                     <th class="ecl-table__header" width="490px">{{ $t("message.methodology.dimensions.table.metric") }}</th>
                     <th class="ecl-table__header weight-row" width="60px">{{ $t("message.methodology.dimensions.table.weight") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <th id="accessUrlStatusCodes" width="148px">{{ $t("message.methodology.dimensions.accessible.keywords_name_1") }}</th>
                     <td width="220px">{{ $t("message.methodology.dimensions.accessible.keywords_desc_1") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.accessible.keywords_metric_1") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dcat:accessURL</span>
                        </div>
                     </td>
                     <td class="weight-row">50</td>
                  </tr>
                  <tr>
                     <th id="downloadUrlStatusCodes">{{ $t("message.methodology.dimensions.accessible.keywords_name_2") }}</th>
                     <td>{{ $t("message.methodology.dimensions.accessible.keywords_desc_2") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.accessible.keywords_metric_2") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dcat:downloadURL</span>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="downloadUrlAvailability">{{ $t("message.methodology.dimensions.accessible.keywords_name_3") }}</th>
                     <td>{{ $t("message.methodology.dimensions.accessible.keywords_desc_3") }}</td>
                     <td>
                        <p>{{ $t("message.methodology.dimensions.accessible.keywords_metric_3") }}</p>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dcat:downloadURL</span>
                        </div>
                     </td>
                     <td class="weight-row">30</td>
                  </tr>
               </tbody>
            </table>
         </div>
         <h4 class="ecl-u-type-heading-4 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-6">{{ $t("message.dashboard.interoperability.title") }}</h4>
         <span v-html="$t('message.methodology.dimensions.interoperable.main_text')"></span>
         <div class="doc-space"></div>
         <div class="table-responsive">
            <table class="table table-hover">
               <thead>
                  <tr class="head-tb">
                     <th width="148px">{{ $t("message.methodology.dimensions.table.metric_name") }}</th>
                     <th width="220px">{{ $t("message.methodology.dimensions.table.desc") }}</th>
                     <th width="430px">{{ $t("message.methodology.dimensions.table.metric") }}</th>
                     <th class="weight-row" width="60px">{{ $t("message.methodology.dimensions.table.weight") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <th id="formatAvailability" width="139px">{{ $t("message.methodology.dimensions.interoperable.keywords_name_1") }}</th>
                     <td width="220px">{{ $t("message.methodology.dimensions.interoperable.keywords_desc_1") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_metric_1')"></span>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:format</span>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="mediaTypeAvailability">{{ $t("message.methodology.dimensions.interoperable.keywords_name_2") }}</th>
                     <td>{{ $t("message.methodology.dimensions.interoperable.keywords_desc_2") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_metric_2')"></span>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dcat:mediaType </span>
                        </div>
                     </td>
                     <td class="weight-row">10</td>
                  </tr>
                  <tr>
                     <th id="formatMediaTypeAlignment">{{ $t("message.methodology.dimensions.interoperable.keywords_name_3") }}</th>
                     <td>{{ $t("message.methodology.dimensions.interoperable.keywords_desc_3") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_metric_3')"></span>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:format </span><br>
                           <span class="dcat-specs-key">dcat:mediaType </span>
                        </div>
                     </td>
                     <td class="weight-row">10</td>
                  </tr>
                  <tr>
                     <th id="formatMediaTypeNonProprietary">{{ $t("message.methodology.dimensions.interoperable.keywords_name_4") }}</th>
                     <td>{{ $t("message.methodology.dimensions.interoperable.keywords_desc_4") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_metric_4')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:format </span><br>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="formatMediaTypeMachineReadable">{{ $t("message.methodology.dimensions.interoperable.keywords_name_5") }}</th>
                     <td>{{ $t("message.methodology.dimensions.interoperable.keywords_desc_5") }} </td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_metric_5')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:format </span><br>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="dcatApCompliance">{{ $t("message.methodology.dimensions.interoperable.keywords_name_6") }}</th>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_desc_6')"></span>
                     </td>
                     <td class="position-relative">
                        <span v-html="$t('message.methodology.dimensions.interoperable.keywords_metric_6')"></span>
                        <div class="dcat-specs">
                           <span>
                              <div class="badge badge-primary position-absolute" style="bottom:5%;right:5%;">DCAT-AP 2.0.1</div>
                           </span>
                        </div>
                     </td>
                     <td class="weight-row">30</td>
                  </tr>
               </tbody>
            </table>
         </div>
         <h4 class="ecl-u-type-heading-4 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-6">{{ $t("message.dashboard.reusability.title") }}</h4>
         <span v-html="$t('message.methodology.dimensions.reuse.main_text')"></span>
         <div class="doc-space"></div>
         <div class="table-responsive">
            <table class="table table-hover">
               <thead>
                  <tr class="head-tb">
                     <th width="148px">{{ $t("message.methodology.dimensions.table.metric_name") }}</th>
                     <th width="260px">{{ $t("message.methodology.dimensions.table.desc") }}</th>
                     <th width="360px">{{ $t("message.methodology.dimensions.table.metric") }}</th>
                     <th class="weight-row" width="60px">{{ $t("message.methodology.dimensions.table.weight") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <th id="licenceAvailability" width="139px">{{ $t("message.methodology.dimensions.reuse.keywords_name_1") }}</th>
                     <td width="258px">{{ $t("message.methodology.dimensions.reuse.keywords_desc_1") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_1')"></span>
                        <div class="dcat-specs" style="margin-top: 20px;">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:license</span>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="licenceAlignment">{{ $t("message.methodology.dimensions.reuse.keywords_name_2") }}</th>
                     <td>{{ $t("message.methodology.dimensions.reuse.keywords_desc_2") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_2')"></span>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:license</span>
                        </div>
                     </td>
                     <td class="weight-row">10</td>
                  </tr>
                  <tr>
                     <th id="accessRightsAvailability">{{ $t("message.methodology.dimensions.reuse.keywords_name_3") }}</th>
                     <td>{{ $t("message.methodology.dimensions.reuse.keywords_desc_3") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_3')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dct:accessRights</span>
                        </div>
                     </td>
                     <td class="weight-row">10</td>
                  </tr>
                  <tr>
                     <th id="accessRightsAlignment">{{ $t("message.methodology.dimensions.reuse.keywords_name_4") }}</th>
                     <td>{{ $t("message.methodology.dimensions.reuse.keywords_desc_4") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_4')"></span>
                        <div class="dcat-specs">
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dct:accessRights</span>
                        </div>
                     </td>
                     <td class="weight-row">5</td>
                  </tr>
                  <tr>
                     <th id="contactPointAvailability">{{ $t("message.methodology.dimensions.reuse.keywords_name_5") }}</th>
                     <td>{{ $t("message.methodology.dimensions.reuse.keywords_desc_5") }} </td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_5')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dcat:contactPoint</span>
                        </div>
                     </td>
                     <td class="weight-row">20</td>
                  </tr>
                  <tr>
                     <th id="publisherAvailability">{{ $t("message.methodology.dimensions.reuse.keywords_name_6") }}</th>
                     <td><span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_6')"></span></td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.reuse.keywords_metric_6')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset") }}</span><br>
                           <span class="dcat-specs-key">dct:publisher</span>
                        </div>
                     </td>
                     <td class="weight-row">10</td>
                  </tr>
               </tbody>
            </table>
         </div>
         <h4 class="ecl-u-type-heading-4 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-6">{{ $t("message.dashboard.contextuality.title") }}</h4>
         <span v-html="$t('message.methodology.dimensions.contextual.main_text')"></span>
         <div class="doc-space"></div>
         <div class="table-responsive">
            <table class="table table-hover">
               <thead>
                  <tr class="head-tb">
                     <th width="160px">{{ $t("message.methodology.dimensions.table.metric_name") }}</th>
                     <th width="220px">{{ $t("message.methodology.dimensions.table.desc") }}</th>
                     <th width="360px">{{ $t("message.methodology.dimensions.table.metric") }}</th>
                     <th class="weight-row" width="60px">{{ $t("message.methodology.dimensions.table.weight") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <th id="rightsAvailability">{{ $t("message.methodology.dimensions.contextual.keywords_name_1") }}</th>
                     <td width="260px">{{ $t("message.methodology.dimensions.contextual.keywords_desc_1") }}</td>
                     <td width="260px">
                        <span v-html="$t('message.methodology.dimensions.contextual.keywords_metric_1')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:rights</span>
                        </div>
                     </td>
                     <td class="weight-row">5</td>
                  </tr>
                  <tr>
                     <th id="byteSizeAvailability">{{ $t("message.methodology.dimensions.contextual.keywords_name_2") }}</th>
                     <td>{{ $t("message.methodology.dimensions.contextual.keywords_desc_2") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.contextual.keywords_metric_2')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.distribution") }}</span><br>
                           <span class="dcat-specs-key">dcat:byteSize</span>
                        </div>
                     </td>
                     <td class="weight-row">5</td>
                  </tr>
                  <tr>
                     <th id="dateIssuedAvailability">{{ $t("message.methodology.dimensions.contextual.keywords_name_3") }}</th>
                     <td>{{ $t("message.methodology.dimensions.contextual.keywords_desc_3") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.contextual.keywords_metric_3')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset_and_distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:issued</span>
                        </div>
                     </td>
                     <td class="weight-row">5</td>
                  </tr>
                  <tr>
                     <th id="dateModifiedAvailability">{{ $t("message.methodology.dimensions.contextual.keywords_name_4") }}</th>
                     <td>{{ $t("message.methodology.dimensions.contextual.keywords_desc_4") }}</td>
                     <td>
                        <span v-html="$t('message.methodology.dimensions.contextual.keywords_metric_4')"></span>
                        <div class="dcat-specs"><br>
                           <span class="dcat-specs-top">{{ $t("message.methodology.dimensions.table.dataset_and_distribution") }}</span><br>
                           <span class="dcat-specs-key">dct:modified</span>
                        </div>
                     </td>
                     <td class="weight-row">5</td>
                  </tr>
               </tbody>
            </table>
         </div>
         <h3 class="ecl-u-type-heading-3 ecl-u-mb-m ecl-u-mt-l" id="inline-nav-6">{{ $t("message.methodology.scoring.table.scoring_cat") }}</h3>
         <p class="ecl-u-type-paragraph-m m-0">       <span v-html="$t('message.methodology.scoring.text')"></span></p>
         <div class="table-responsive">
            <table class="table md-tb" width="450px">
               <thead>
                  <tr class="head-tb">
                     <th width="260px">{{ $t("message.methodology.scoring.table.dimension") }}</th>
                     <th>{{ $t("message.methodology.scoring.table.max_points") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr>
                     <th>{{ $t("message.dashboard.findability.title") }}</th>
                     <td>100</td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.dashboard.accessibility.title") }}</th>
                     <td>100</td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.dashboard.interoperability.title") }}</th>
                     <td>110</td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.dashboard.reusability.title") }}</th>
                     <td>75 </td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.dashboard.contextuality.title") }}</th>
                     <td>20 </td>
                  </tr>
                  <tr class="sum">
                     <th>{{ $t("message.methodology.scoring.table.sum") }}</th>
                     <td><strong>405</strong></td>
                  </tr>
               </tbody>
            </table>
         </div>
         <div class="doc-space"></div>
         <span v-html="$t('message.methodology.scoring.text')"></span>
         <div class="doc-space"></div>
         <div style="margin-bottom:70px;" class="table-responsive">
            <table class="table sm-tb">
               <thead>
                  <tr class="head-tb">
                     <th scope="col" width="160px">{{ $t("message.methodology.scoring.table.scoring_cat") }}</th>
                     <th scope="col">{{ $t("message.methodology.scoring.table.range") }}</th>
                  </tr>
               </thead>
               <tbody>
                  <tr style="backgroundcolor: #000;">
                     <th>{{ $t("message.methodology.scoring.table.excellent") }}</th>
                     <td>351 - 405</td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.methodology.scoring.table.good") }}</th>
                     <td>221 – 350</td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.methodology.scoring.table.sufficient") }}</th>
                     <td>121 – 220</td>
                  </tr>
                  <tr>
                     <th>{{ $t("message.methodology.scoring.table.bad") }}</th>
                     <td>0 - 120</td>
                  </tr>
               </tbody>
            </table>
         </div>
      </div>
   </div>
</div>
</template>
<script>
import AppLink from '../AppLink.vue'
export default {
  name: 'Inpage-navigation',
  components: {
    AppLink
  },
  data () {
    return {
      rootURL: this.$env.ROOT_URL,
      shaclValidatorUrl: this.$env.SHACL_VALIDATOR_URL,
      shaclApiUrl: this.$env.SHACL_API_URL
    }
  }
}
</script>
<style scoped>
.ecl-table {
    border: 1px solid #EBEBEB;
}
.ecl-u-type-paragraph-m {
  max-width: 100%!important;
}
.ecl-inpage-navigation__title {
  padding-top: 0;
}
.ecl-u-mb-m ecl-u-mt-l {
    margin-bottom: 18px!important;
}
</style>
