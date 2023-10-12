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
              beginAtZero: true,
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
              display: false
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
        /*legend: {
          display: true,
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
        },*/
        legend: false,
        legendCallback: (chart) => {
            var text = [];
                  text.push('<ul class="ul-line-chart-country">');
                  for (let i=0; i<chart.data.datasets.length; i++) {
                      let classname = "line-chart-country-clickable-legend-text-"+i
                      text.push('<li class="line-chart-country-clickable-legend">');
                      text.push('<span style="background-color:' + chart.data.datasets[i].backgroundColor + '"></span>');
                      text.push('<span id="'+classname+'" style="cursor: default;">');
                      text.push(chart.data.datasets[i].label);
                      text.push('</span>');
                      text.push('</li>');
                  }
                  text.push('</ul>');
                  return text.join("");
          },
          /*layout: {
              padding: {
                  left: 20,
                  right: 40,
                  top: 50,
                  bottom: 20
              }
          },*/
        responsive: true,
        maintainAspectRatio: false
        
      },
      chartLabelColors: ["#b0e0f2", "#53a4c1", "#21576b", "#a2efdb", "#5caa95", "#147259", "#efc36b", "#eae198", "#c6711b", "#894a0b", "#c95091", "#93225e", "#580e93", "#8f45cc", "#ea98dc", "#ce4466", "#e03a48", "#b50514", "#7c000a", "#7eb269", "#478e2a", "#24660b", "#164702", "#6f84b7", "#3a5baf", "#052984", "#0e2254", "#ed8a50", "#ef5a04", "#ba4400", "#7bf3f7", "#1bcfd6", "#009196", "#026266", "#5b7c7c", "#041e1e", "#b1e800", "#88b200", "#ead700", "#b2a300", "#7f7400", "#3a3500", "#0099b5", "#007287", "#00434f", "#ea00ea", "#ba00ba", "#7a007a", "#540054", "#f20014", "#bc000f", "#770009", "#f45d00", "#b54500", "#823100", "#421900", "#000be5", "#0008af", "#000570", "#00023a", "#00e073", "#00aa58", "#00773d", "#00592e", "#ea7575", "#b75252", "#8e3333", "#efc9c9", "#826565", "#696582", "#9a95bf", "#514e66", "#9fcccb", "#5b7777"]
    }
  },
  mounted () {
    // this.a_gradient = this.$refs.canvas.getContext('2d').createLinearGradient(0, 0, 0, 450)
    // this.a_gradient.addColorStop(0, 'red')
    // this.a_gradient.addColorStop(0.5, 'white')
    // this.a_gradient.addColorStop(1, 'red')

    // Create datasets array
    const datasets = [];

    // Fill datasets array with data from catalogues (chartlabels)
    this.chartlabels.forEach((cl, index) => {
      datasets.push({
          label: cl.name,
          data: this.chartdata[index],
          borderColor: this.chartLabelColors[index],
          backgroundColor: this.chartLabelColors[index],
          fill: false
        });
    });
    
    // Render chart
    this.renderChart({
      labels: this.date,
      datasets,
    }, this.options)
      document.getElementById('line-chart-country-js-legend').innerHTML = this.generateLegend();
      let chart = this.$data._chart
      var onClick = function (e, index) {
          var ci = chart
          var alreadyHidden = (ci.getDatasetMeta(index).hidden === null) ? false : ci.getDatasetMeta(index).hidden;
          var anyOthersAlreadyHidden = false;
          var allOthersHidden = true;

          // figure out the current state of the labels
          ci.data.datasets.forEach(function (e, i) {
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
              document.getElementById("line-chart-country-clickable-legend-text-"+index).classList.remove("hidden");
          } else {
              // otherwise, lets figure out how to toggle visibility based upon the current state
              ci.data.datasets.forEach(function (e, i) {
                  var meta = ci.getDatasetMeta(i);
                  document.getElementById("line-chart-country-clickable-legend-text-"+i).classList.add("hidden");
                  if (i !== index) {
                      // handles logic when we click on visible hidden label and there is currently at least
                      // one other label that is visible and at least one other label already hidden
                      // (we want to keep those already hidden still hidden)
                      if (anyOthersAlreadyHidden && !allOthersHidden) {
                          document.getElementById("line-chart-country-clickable-legend-text-"+i).classList.add("hidden");
                          meta.hidden = true;
                      } else {
                          // toggle visibility
                          meta.hidden = meta.hidden === null ? !meta.hidden : null;
                          if (meta.hidden == null){
                              document.getElementById("line-chart-country-clickable-legend-text-"+i).classList.remove("hidden");
                          }
                      }
                  } else {
                      meta.hidden = null;
                      document.getElementById("line-chart-country-clickable-legend-text-"+i).classList.remove("hidden");
                  }
              });
          }
          ci.update();
      }
      const legendItems = [
          ...document.querySelectorAll(".line-chart-country-clickable-legend")
      ];

      legendItems.forEach((item, i) => {
          item.addEventListener("click", (e) =>
              onClick(e, i)
          );
      });
  },
  methods: {
  }
}
</script>
