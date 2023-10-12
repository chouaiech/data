import re
import datetime
import logging


class DataManipulation:

    def __init__(self):
        self.log = logging.getLogger("DateManipulation")

    def filter_between_dates(self, start_date, end_date, dataset):
        start_date = self.get_date_from_string(start_date)
        end_date = self.get_date_from_string(end_date)
        filtered_data = []
        for data in dataset:
            data_date = self.get_date_from_string(data[0])
            if start_date <= data_date <= end_date:
                filtered_data.append(data)
        return filtered_data

    def filter_interval_in_days(self, interval, compare_date, dataset):
        filtered_data = []
        try:
            compare_date = self._find_first_date(self.get_date_from_string(compare_date),
                                                 self.get_date_from_string(dataset[0][0]),
                                                 interval)
            previous_date = compare_date
            for data in dataset:
                data_date = self.get_date_from_string(data[0])
                if data_date > previous_date:
                    compare_date = self._find_first_date(compare_date, data_date, interval)
                if data_date == compare_date:
                    filtered_data.append(data)
                    compare_date += datetime.timedelta(days=interval)
                previous_date = data_date + datetime.timedelta(days=interval)
            return filtered_data
        except IndexError:
            return filtered_data

    def get_date_from_string(self, string):
        date_regex = r'[1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]'
        try:
            ds = re.search(date_regex, string).group(0)
            return datetime.date(int(ds[:4]), int(ds[5:7]), int(ds[-2:]))
        except AttributeError or TypeError:
            print("Date-Format not valid!")
            return None

    def _find_first_date(self, start_date, data_date, interval):
        if start_date >= data_date:
            return start_date
        else:
            while start_date < data_date:
                start_date += datetime.timedelta(days=interval)
            return start_date
