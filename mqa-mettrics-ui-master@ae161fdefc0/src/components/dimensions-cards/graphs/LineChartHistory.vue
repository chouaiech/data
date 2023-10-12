<script>
import { Line } from 'vue-chartjs'
import 'chartjs-plugin-labels'
export default {
  extends: Line,
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
    title: {
      type: String,
      default: '[No title]',
      required: false
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
    }
  },
  data () {
    return {
      options: {
        title: {
          display: false,
          text: this.title
        },
        tooltips: {
          // Toolbar an/aus
          enabled: true
        },
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true,
              stepSize: parseInt(this.$env.SCORING_STEP_SIZE),
              max: 100
            },
            gridLines: {
              display: true
            }
          }],
          xAxes: [{
            gridLines: {
              display: true
            }
          }]
        },
        legend: {
          display: false
        },
        responsive: true,
        maintainAspectRatio: false,
        cutoutPercentage: 10,
        plugins: {
          labels: {
            render: 'percentage',
            fontColor: '#000',
            position: 'outside'
          }
        }
      }
    }
  },
  mounted () {
    this.renderChart(
      {
        labels: this.chartLabels,
        datasets: [
          {
            data: this.chartData,
            backgroundColor: this.chartBackgroundColors[0]
          },
          {
            data: this.yes,
            backgroundColor: 'white'
          },
          {
            data: this.no,
            backgroundColor: 'black'
          }
        ]
      },
      this.options
    )
  }
}
</script>
