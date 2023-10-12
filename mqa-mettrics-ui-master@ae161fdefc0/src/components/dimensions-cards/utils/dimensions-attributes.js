// Attribute values of all dimensions to expand metrics data
// with additional/prior knowledge.

// Helper object to describe which colors the graphs get
// The graphs can't refer to SCSS variables so we describe them here manually
const chartbgColors = {
  // Define primary colors for each dimension
  accessibility: '#004494',//'#4A82FA',
  findability: '#004494',//'#00997A',
  interoperability: '#004494',//'#001D85',
  reusability: '#004494',//'#DC5149',
  contextuality: '#004494',//'#ffcc00',
  score: '#004494',//'#4D4F5C',
  // secondary colors every graph will get
  // i.e., non-primary or non-important colors
  secondary: [
    'rgb(242, 242, 242)',
    'rgb(159, 242, 223)'
  ]
}

const GRAPH_TYPE = {
  bar: 'bar-chart',
  doughnut: 'doughnut-chart',
  line: 'line-chart',
  lineHistory: 'line-chart-history'
}

const REDUCE_FUNCTION = {
  max (values) {
    if (!values) return null
    return values.reduce((prev, current) => (prev.percentage > current.percentage) ? prev : current)
  }
}

const dimensionsAttributes = {
  score: {
    translationKey: 'scoring',
    label: 'Scoring',
    className: 'scoring',
    description: 'The Time Based Scoring Chart.',
    chartStyle: {
      backgroundColors: [
        chartbgColors.score,
        // ...chartbgColors.secondary
      ]
    },
    indicators: {
      timeBasedScoring: {
        label: '',
        graphType: GRAPH_TYPE.line
      }
    }
  },
  accessibility: {
    translationKey: 'accessibility',
    label: 'Accessibility',
    className: 'accessibility',
    description: 'Expedita eaque illo sint explicabo quibusdam.',
    chartStyle: {
      backgroundColors: [
        chartbgColors.accessibility,
        ...chartbgColors.secondary
      ]
    },
    indicators: {
      accessUrlStatusCodes: {
        label: '',
        tooltipDescription: 'accessible.keywords_desc_1',
        tooltipMetrics: 'accessible.keywords_metric_1',
        displayName: true,
        primary: REDUCE_FUNCTION.max,
        graphType: GRAPH_TYPE.bar,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      downloadUrlAvailability: {
        label: '',
        tooltipDescription: 'accessible.keywords_desc_2',
        tooltipMetrics: 'accessible.keywords_metric_2',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      downloadUrlStatusCodes: {
        label: '',
        tooltipDescription: 'accessible.keywords_desc_3',
        tooltipMetrics: 'accessible.keywords_metric_3',
        displayName: true,
        primary: REDUCE_FUNCTION.max,
        graphType: GRAPH_TYPE.bar,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      }
    }
  },
  findability: {
    translationKey: 'findability',
    label: 'Findability',
    className: 'findability',
    description:
        'Minus molestias, sapiente quaerat voluptate suscipit expedita eaque illo sint explicabo quibusdam eligendi dolorem.',
    chartStyle: {
      backgroundColors: [
        chartbgColors.findability,
        ...chartbgColors.secondary
      ]
    },
    indicators: {
      temporalAvailability: {
        label: '',
        tooltipDescription: 'findability.keywords_desc_4',
        tooltipMetrics: 'findability.keywords_metric_4',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      spatialAvailability: {
        label: '',
        tooltipDescription: 'findability.keywords_desc_3',
        tooltipMetrics: 'findability.keywords_metric_3',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      keywordAvailability: {
        label: '',
        tooltipDescription: 'findability.keywords_desc_1',
        tooltipMetrics: 'findability.keywords_metric_1',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      categoryAvailability: {
        label: '',
        tooltipDescription: 'findability.keywords_desc_2',
        tooltipMetrics: 'findability.keywords_metric_2',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      }
    }
  },
  interoperability: {
    translationKey: 'interoperability',
    label: 'Interoperability',
    className: 'interoperability',
    description:
        'Lorem ipsum dolor sit, amet consectetur adipisicing elit.',
    chartStyle: {
      backgroundColors: [
        chartbgColors.interoperability,
        ...chartbgColors.secondary
      ]
    },
    indicators: {
      formatMediaTypeNonProprietary: {
        label: '',
        tooltipDescription: 'interoperable.keywords_desc_4',
        tooltipMetrics: 'interoperable.keywords_metric_4',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      formatMediaTypeAlignment: {
        label: '',
        tooltipDescription: 'interoperable.keywords_desc_3',
        tooltipMetrics: 'interoperable.keywords_metric_3',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      formatMediaTypeMachineReadable: {
        label: '',
        tooltipDescription: 'interoperable.keywords_desc_5',
        tooltipMetrics: 'interoperable.keywords_metric_5',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      dcatApCompliance: {
        label: '',
        tooltipDescription: 'interoperable.keywords_desc_6',
        tooltipMetrics: 'interoperable.keywords_metric_6',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      mediaTypeAvailability: {
        label: '',
        tooltipDescription: 'interoperable.keywords_desc_2',
        tooltipMetrics: 'interoperable.keywords_metric_2',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      formatAvailability: {
        label: '',
        tooltipDescription: 'interoperable.keywords_desc_1',
        tooltipMetrics: 'interoperable.keywords_metric_1',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      }
    }
  },
  reusability: {
    translationKey: 'reusability',
    label: 'Reusability',
    className: 'reusability',
    description:
        'Lorem ipsum dolor sit, amet consectetur adipisicing elit. Minus molestias, sapiente.',
    chartStyle: {
      backgroundColors: [
        chartbgColors.reusability,
        ...chartbgColors.secondary
      ]
    },
    indicators: {
      contactPointAvailability: {
        label: '',
        tooltipDescription: 'reuse.keywords_desc_5',
        tooltipMetrics: 'reuse.keywords_metric_5',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      licenceAvailability: {
        label: '',
        tooltipDescription: 'reuse.keywords_desc_1',
        tooltipMetrics: 'reuse.keywords_metric_1',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      licenceAlignment: {
        label: '',
        tooltipDescription: 'reuse.keywords_desc_2',
        tooltipMetrics: 'reuse.keywords_metric_2',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      accessRightsAvailability: {
        label: '',
        tooltipDescription: 'reuse.keywords_desc_3',
        tooltipMetrics: 'reuse.keywords_metric_3',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      publisherAvailability: {
        label: '',
        tooltipDescription: 'reuse.keywords_desc_6',
        tooltipMetrics: 'reuse.keywords_metric_6',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      accessRightsAlignment: {
        label: '',
        tooltipDescription: 'reuse.keywords_desc_4',
        tooltipMetrics: 'reuse.keywords_metric_4',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      }
    }
  },
  contextuality: {
    translationKey: 'contextuality',
    label: 'Contextuality',
    className: 'contextual',
    description: 'Lorem ipsum dolor sit, amet.',
    chartStyle: {
      backgroundColors: [
        chartbgColors.contextuality,
        ...chartbgColors.secondary
      ]
    },
    indicators: {
      dateIssuedAvailability: {
        label: '',
        tooltipDescription: 'contextual.keywords_desc_3',
        tooltipMetrics: 'contextual.keywords_metric_3',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      dateModifiedAvailability: {
        label: '',
        tooltipDescription: 'contextual.keywords_desc_4',
        tooltipMetrics: 'contextual.keywords_metric_4',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      byteSizeAvailability: {
        label: '',
        tooltipDescription: 'contextual.keywords_desc_2',
        tooltipMetrics: 'contextual.keywords_metric_2',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      },
      rightsAvailability: {
        label: '',
        tooltipDescription: 'contextual.keywords_desc_1',
        tooltipMetrics: 'contextual.keywords_metric_1',
        graphType: GRAPH_TYPE.doughnut,
        graphTypeHistory: GRAPH_TYPE.lineHistory
      }
    }
  }
}

const dimensionsAttributesOrdered = {
  score: dimensionsAttributes.score,
  findability: dimensionsAttributes.findability,
  accessibility: dimensionsAttributes.accessibility,
  interoperability: dimensionsAttributes.interoperability,
  reusability: dimensionsAttributes.reusability,
  contextuality: dimensionsAttributes.contextuality
}

export default dimensionsAttributesOrdered
