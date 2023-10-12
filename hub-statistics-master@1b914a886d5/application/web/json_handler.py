import logging
import json
import numpy
from datetime import date
from util.app_configuration import AppConfiguration
from util.identifier import Identifier
from data_store.database_handler import DatabaseHandler


class JsonHandler:

    def __init__(self):
        self._log = logging.getLogger("JsonHandler")
        self._config = AppConfiguration()
        self._database_handler = DatabaseHandler()

    def create_json(self, identifier: str,
                    start_date: date = date.fromisoformat("1970-01-01"),
                    end_date: date = date.today()):
        df = self._database_handler.get_data_from_identifier(identifier, start_date, end_date)
        return self._build_json(df, identifier)

    def _build_json(self, df, identifier) -> json:
        response = {"sum": self._calc_sum(df, identifier)}
        result_array = []
        for name_index in df.index:
            result_object = self._evaluate_index_name(name_index, identifier)
            stats_array = []
            for date_index in df:
                stats_array.append(self._count_values(name_index, date_index, df, identifier))
            result_object["stats"] = stats_array
            result_array.append(result_object)
        response["result"] = result_array
        if identifier == Identifier.NUM_DATASETS:
            return json.dumps(response["result"][0])
        else:
            return json.dumps(response)

    def _count_values(self, name_index, date_index, df, identifier):
        if identifier == Identifier.DS_ASSIGNED_TO_CATEGORY or \
                identifier == Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY:
            if date_index[-2:] == " 2" or date_index[-2:] == " 3":
                return
            else:
                result = {"date": date_index[:-2], "assigned_datasets": df.at[name_index, date_index[:-2] + " 1"],
                          "total_datasets": df.at[name_index, date_index[:-2] + " 2"]}
                result["assigned_datasets"] = numpy.nan_to_num(result["assigned_datasets"])
                result["total_datasets"] = numpy.nan_to_num(result["total_datasets"])
        else:
            result = {"date": date_index, "count": df.at[name_index, date_index]}
            result["count"] = numpy.nan_to_num(result["count"])
        return result

    def _calc_sum(self, df, identifier):
        if identifier == Identifier.DS_ASSIGNED_TO_CATEGORY or \
                identifier == Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY:
            return df[df.columns[-2]].sum()
        else:
            return df[df.columns[-1]].sum()

    def _evaluate_index_name(self, name_index, identifier):
        if identifier == Identifier.DS_PER_COUNTRY_AND_CATALOGUE or \
                identifier == Identifier.DS_PER_COUNTRY_AND_CATEGORY or \
                identifier == Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY or \
                identifier == Identifier.DS_ASSIGNED_TO_CATEGORY or \
                identifier == Identifier.DS_PER_ISO_COUNTRY:
            return {"name": name_index.split(": ")[1], "spatial": name_index.split(": ")[0].lower()}
        else:
            return {"name": name_index}
