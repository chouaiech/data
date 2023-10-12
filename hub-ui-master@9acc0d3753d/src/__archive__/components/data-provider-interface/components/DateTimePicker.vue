<script>
import DatePicker from './DatePicker';

export default {
  extends: DatePicker,
  name: 'DateTimePicker',
  data() {
    return {
      type: 'datetime',
      format: 'YYYY-MM-DD HH:mm',
      valueType: 'YYYY-MM-DDThh:mm',
    };
  },
}
</script>
