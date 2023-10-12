<script>
import { Doughnut } from 'vue-chartjs'
import 'chartjs-plugin-labels';

export default {
  extends: Doughnut,
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
        scales: {},
        legend: {
          display: false
        },
        responsive: true,
        maintainAspectRatio: false,
        cutoutPercentage: 10,
        plugins: {
          labels: {
            render: 'percentage',
            fontColor: ['white', 'black']
            // position: 'outside'
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
