<script>
import { Bar } from 'vue-chartjs';
import 'chartjs-plugin-labels';

export default {
  extends: Bar,
  props: {
    // eslint-disable-next-line vue/require-default-prop
    chartData: {
      type: [Array, Object],
      required: true
    },
    chartLabels: {
      type: Array,
      required: true
    },
    chartBackgroundColors: {
      type: Array,
      required: false,
      default () {
        return [
          'rgb(41, 114, 170)',
          'rgb(242, 242, 242)',
          'rgb(159, 242, 223)'
        ]
      }
    },
    title: {
      type: String,
      default: '[No title]',
      required: false
    }
  },
  computed: {
    options() {
      return {
        title: {
          display: false,
          text: this.title
        },
        tooltips: {
          // Toolbar an/aus
          enabled: true
        },
        legend: {
          display: false
        },
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          xAxes: [{
            stacked: true,
            ticks: {
              // this will fix your problem with NaN
              callback: function (label) {
                return label || ''
              }
            }
          }],
          yAxes: [{
            ticks: {
              beginAtZero: true,
              max: 100
            }
          }]
        },
        plugins: {
          labels: {
            render: function (args) {
              let max = 100 // Custom maximum value
              return Math.round(args.value * 100 / max) + "%"
            }
          }
        }
      };
      }
  },
  mounted () {
    this.renderChart(
      {
        labels: this.chartLabels,
        datasets: [
          {
            label: '',
            data: this.chartData,
            backgroundColor: this.chartBackgroundColors[0]
          }
        ]
      },
      this.options
    )
  }
}
</script>
