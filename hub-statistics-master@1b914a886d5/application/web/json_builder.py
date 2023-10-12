import logging
from util.app_configuration import AppConfiguration
from util.category import Category
from util.spatial import Spatial
from util.identifier import Identifier


class JsonBuilder:

    def __init__(self):
        self._log = logging.getLogger("JsonBuilder")
        self._config = AppConfiguration()

    def build_json(self, col_names, data) -> dict:
        if len(col_names) != len(data[0]):
            return {}
        json_data = {}
        for i in range(1, len(col_names)):
            col_name = col_names[i]
            col_list = []
            for j in range(0, len(data)):
                col_data = {}
                row = data[j]
                col_data["date"] = row[0]
                col_data["count"] = row[i]
                col_list.append(col_data)
            json_data[col_name] = col_list
        return json_data

    def build_packed_json(self, identifier, col_names, col_data) -> dict:
        if len(col_names) != len(col_data[0]):
            return {}
        json_list: list = []
        sum = 0
        for i in range(1, len(col_names)):
            col_name = col_names[i]
            col_list = []
            data = {}
            if identifier in [Identifier.DS_PER_COUNTRY_AND_CATALOGUE, Identifier.DS_ASSIGNED_TO_CATEGORY,
                              Identifier.DS_PER_COUNTRY_AND_CATEGORY, Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY]:
                data["name"] = col_name[3:]
                data["spatial"] = col_name[:2].lower()
            elif identifier in [Identifier.DS_PER_ISO_COUNTRY]:
                data["name"] = col_name
                data["spatial"] = Spatial.get_iso_data().get(col_name)
            else:
                data["name"] = col_name
            for j in range(0, len(col_data)):
                count_data = {}
                row = col_data[j]
                count_data["date"] = row[0]
                count_data["count"] = row[i]
                if identifier in [Identifier.DS_ASSIGNED_TO_CATEGORY, Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY] \
                        and row[i] is not None \
                        and j == (len(col_data) - 1):
                    sum += int(row[i].split('/')[0])
                else:
                    if row[i] is not None and j == (len(col_data) - 1):
                        sum += int(row[i])
                col_list.append(count_data)
            data["stats"] = col_list
            json_list.append(data)
        return_json = {"sum": sum, "result": json_list}
        return return_json
