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
      type: Array
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
              max: parseInt(this.$env.SCORING_MAX_POINTS)
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
            backgroundColor: this.chartBackgroundColors
            // backgroundColor: [
              // 'rgb(142, 142, 142)'
            // ]
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
