<script>
import { Line } from 'vue-chartjs'
/* eslint-disable */

export default {
  extends: Line,
  props: {
    chartdata: {
      type: Array | Object,
      default: null
    },
    chartlabels: {
      type: Array,
      required: true
    },
    date: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: false,
              callback: function(value) {
                return value.toLocaleString('fi');
              }
            },
            gridLines: {
              display: true
            }
          }],
          xAxes: [ {
            gridLines: {
              display: true
            }
          }]
        },
        tooltips: {
          callbacks: {
            label: function(tooltipItem, data) {
              var label = data.datasets[tooltipItem.datasetIndex].label || '';
              if (label) label += ': ';
              label += (Math.round(tooltipItem.yLabel * 100) / 100).toLocaleString('fi');
              return label;
            }
          }
        },
        legend: {
          display: false,
          position: 'left',
          onClick: function(e, legendItem) {
          var index = legendItem.datasetIndex;
          var ci = this.chart;
          var alreadyHidden = (ci.getDatasetMeta(index).hidden === null) ? false : ci.getDatasetMeta(index).hidden;
          var anyOthersAlreadyHidden = false;
          var allOthersHidden = true;

          // figure out the current state of the labels
          ci.data.datasets.forEach(function(e, i) {
            var meta = ci.getDatasetMeta(i);

            if (i !== index) {
              if (meta.hidden) {
                anyOthersAlreadyHidden = true;
              } else {
                allOthersHidden = false;
              }
            }
          });
          // if the label we clicked is already hidden
          // then we now want to unhide (with any others already unhidden)
          if (alreadyHidden) {
            ci.getDatasetMeta(index).hidden = null;
          } else {
            // otherwise, lets figure out how to toggle visibility based upon the current state
            ci.data.datasets.forEach(function(e, i) {
              var meta = ci.getDatasetMeta(i);

              if (i !== index) {
                // handles logic when we click on visible hidden label and there is currently at least
                // one other label that is visible and at least one other label already hidden
                // (we want to keep those already hidden still hidden)
                if (anyOthersAlreadyHidden && !allOthersHidden) {
                  meta.hidden = true;
                } else {
                  // toggle visibility
                  meta.hidden = meta.hidden === null ? !meta.hidden : null;
                }
              } else {
                meta.hidden = null;
              }
            });
          }
          ci.update();
        },
        },
        responsive: true,
        maintainAspectRatio: false
      }
    }
  },
  mounted () {
    this.renderChart({
      labels: this.date,
      datasets: [
        {
         label: "Total",
          data: this.chartdata[0],
          borderColor: '#b0e0f2',
          backgroundColor: '#b0e0f2',
          fill: false
        }
      ]
    }, this.options)
  },
  methods: {
  }
}
</script>
