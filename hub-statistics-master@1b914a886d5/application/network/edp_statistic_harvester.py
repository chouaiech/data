import json
import os
import requests
import requests.auth
import logging
from util.app_configuration import AppConfiguration
from util.category import Category
from datetime import date
from data_store.database_connector import DatabaseConnector


class EdpStatisticHarvester:

    def __init__(self):
        self._log = logging.getLogger("EdpStatisticHarvester")
        self._config = AppConfiguration()
        self._databse_connector = DatabaseConnector()
        self._is_auth_required = os.environ.get("HUB_AUTH_REQUIRED", "false")
        self._hub_user = os.environ.get("HUB_USER")
        self._hub_password = os.environ.get("HUB_PASSWORD")
        self._hub_main_url = os.environ.get("HUB_MAIN_URL", "https://localhost:8080/search")
        self._hub_catalog_uri = os.environ.get("HUB_CATALOG_URI", "filter=catalogue&limit=1000")
        self._hub_dataset_uri = os.environ.get("HUB_DATASET_URI", "/search?q=&filter=dataset&facets={{\"catalog\":[\"{}\"]}}")
        self._hub_all_dataset_uri = os.environ.get("HUB_DATASET_URI", "filter=dataset&limit=1000")

    def start_all_request_to_edp(self):
        self._log.info("Start requesting to edp ...")

        self._log.debug("Is an authorization required? " + self._is_auth_required)
        if self._is_auth_required == "true":
            self._log.debug("Username: " + self._hub_user)
            self._log.debug("Password: " + self._hub_password)

        # Get all over dataset number
        datasets = self._get_num_of_all_datasets()
        self._log.info("Request for number of all datasets completed.")
        self._log.debug(datasets)

        # Get country, catalog and category over all data
        basic_data = self._get_basic_data_of_all_datasets()
        self._log.debug("Request for basic data completed.")
        self._log.debug(json.dumps(basic_data))

        # Get all catalogs
        catalog_metadata = self._get_all_catalogs()
        self._log.info("Got informations about {} catalogs.".format(len(catalog_metadata)))
        self._log.debug(json.dumps(catalog_metadata))

        # Get metadata for each catalog
        self._get_catalog_specific_metadata(catalog_metadata)
        self._log.info("Requests for catalog specific metadata completed.")
        self._log.debug(json.dumps(catalog_metadata))

        # Get metadata for countries
        country_metadata = self._get_country_specific_metadata()
        self._log.info("Request for contry specific metadata completed.")
        self._log.debug(json.dumps(country_metadata))

        # Get metadata for categories aqui
        category_metadata = self._get_category_specific_metadata()
        self._log.info("Request for category specific metadata completed.")
        self._log.debug(json.dumps(category_metadata))

        self._log.info("Requesting to edp finished.")
        self._metadata_processing(datasets, basic_data, catalog_metadata, country_metadata, category_metadata)

    def _metadata_processing(self, datasets: int, basic_data: list, catalog_metadata: dict,
                             country_metadata: dict, category_metadata: dict):
        self._log.info("Start processing the received metadata.")
        self._process_number_of_all_datasets(datasets)
        self._process_basic_data(basic_data)
        self._process_catalogs(catalog_metadata)
        self._process_datasets_per_category(category_metadata)
        self._process_datasets_per_catalogue(catalog_metadata)
        self._process_dataset_per_country(country_metadata)
        self._process_dataset_per_country_and_category(country_metadata)
        self._process_dataset_per_country_and_catalogue(catalog_metadata)
        self._process_assigned_datasets_to_categories_per_contry(country_metadata)
        self._process_assigned_datasets_to_categories_per_country_and_catalogue(catalog_metadata)
        self._log.info("Metadata processing finished.")

    def _get_num_of_all_datasets(self) -> int:
#        req_body = {"filter": "dataset", "catalog": []}
#        self._log.info("_get_num_of_all_datasets")
#        self._log.info(self._hub_main_url)
#        response = self._send_post_request(self._hub_main_url, json.dumps(req_body))
        response = self._send_get_request(self._get_url_for_dataset_request())
        return response["result"]["count"]

    def _get_basic_data_of_all_datasets(self) -> list:
        #req_body = {"filter": "dataset", "catalog": []}
        #response = self._send_post_request(self._hub_main_url, json.dumps(req_body)).get("result")
        response = self._send_get_request(self._get_url_for_dataset_request()).get("result")
        del response["count"]
        del response["results"]
        response = response["facets"]
        return response

    def _get_category_specific_metadata(self) -> dict:
        result = {}
        #req_body = {"filter": "dataset", "limit": 1}
        #response = self._send_post_request(self._hub_main_url, json.dumps(req_body))
        response = self._send_get_request(self._get_url_for_specific_metadata())        
        facets = response["result"]["facets"]
        for face in facets:  # :)
            if face["id"] == "categories":
                categories = face["items"]
                for category in categories:
                    result[category["id"]] = {
                        "title": category["title"],
                        "count": category["count"]
                    }
        return result


    def _get_country_specific_metadata(self) -> dict:
        result = {}
        #req_body = {"filter": "dataset", "limit": 1}
        #response = self._send_post_request(self._hub_main_url, json.dumps(req_body))
        response = self._send_get_request(self._get_url_for_specific_metadata())
        self._log.debug(json.dumps(response["result"]["facets"][3]))
        facets = response["result"]["facets"]
        for face in facets:  # :)
            if face["id"] == "country":
                countries = face["items"]
                for country in countries:
                    meta_data = {}
                    #req_body = {"filter": "dataset", "facets": {"country": [country["id"]]}}
                    #response = self._send_post_request(self._hub_main_url, json.dumps(req_body))
                    response = self._send_get_request(self._get_url_for_country(country["id"]))
                    categories = [obj["items"] for obj in response["result"]["facets"] if obj["id"] == "categories"][0]
                    for delivered_categories in categories:
                        meta_data[delivered_categories["id"].lower()] = delivered_categories["count"]
                    for category in Category.get_catgory_keywords():
                        if category not in meta_data:
                            meta_data[category.lower()] = 0
                    meta_data["title"] = country["title"]
                    meta_data["count"] = country["count"]
                    result[country["id"]] = meta_data
        return result
    
    def _get_url_for_specific_metadata(self):
        return self._hub_main_url + "?" + "filter=dataset&limit=1"
    
    def _get_url_for_country(self, country_id):
        return self._hub_main_url + "?" + "/search?q=&filter=dataset&facets={\"country\":[\""+country_id+"\"]}"

    def _get_catalog_specific_metadata(self, catalogs: dict):
        for catalog in catalogs.keys():
            self._log.debug("Grab Data for catalog: {}".format(catalog))
            #req_body = {"filter": "dataset", "facets": {"catalog": [catalog]}}
            #res = self._send_post_request(self._hub_main_url, json.dumps(req_body))
            res = self._send_get_request(self._get_url_for_metadata_request(catalog))
            meta_data = catalogs[catalog]
            meta_data["count"] = res["result"]["count"]

            categories = [obj["items"] for obj in res["result"]["facets"] if obj["id"] == "categories"][0]
            for category in Category.get_catgory_keywords():
                for cat in categories:
                    if cat["id"].lower() == category.lower():
                        meta_data[category] = cat["count"]
                if category not in meta_data:
                    meta_data[category] = 0
            catalogs[catalog] = meta_data

    def _get_all_catalogs(self):
        res = self._send_get_request(self._get_url_for_catalog_request())
        result = res["result"]["results"]
        catalogs = {}
        for entry in result:
            catalog_details: dict = {}
            if entry["country"] is not None:
                catalog_details["country"] = entry["country"]["id"]
            else:
                catalog_details["country"] = None
            self._log.debug(json.dumps(entry))
            catalog_details["title"] = self._get_title_of_catalog(entry["id"],
                                                                  entry["title"],
                                                                  catalog_details["country"])
            catalogs[entry["id"]] = catalog_details
        return catalogs

    def _get_url_for_metadata_request(self, catalog_id):
        return self._hub_main_url + "?" + self._hub_dataset_uri.format(catalog_id)

    def _get_url_for_catalog_request(self):
        return self._hub_main_url + "?" + self._hub_catalog_uri
    
    def _get_url_for_dataset_request(self):
        return self._hub_main_url + "?" + self._hub_all_dataset_uri

    def _process_basic_data(self, basic_data: list):
        self._log.info("Processing basic metadata.")
        for information in basic_data:
            if information["id"] == "country":
                self._process_countries(information["items"])
            # elif information["id"] == "catalog":
            #     self._process_catalogs(information["items"])
            elif information["id"] == "categories":
                self._process_categories(information["items"])

    def _process_countries(self, country_list: list):
        self._log.info("Processing country data.")
        database_list = []
        for county in country_list:
            database_list.append({"country_id": county["id"], "name": county["title"]})
        self._databse_connector.insert_countries(database_list)

    def _process_catalogs(self, catalog_list: dict):
        self._log.info("Processing catalog data.")
        database_list = []
        for catalog_id in catalog_list.keys():
            country_id = None if catalog_list[catalog_id]["country"] is None else catalog_list[catalog_id]["country"]
            database_list.append({"catalog_id": catalog_id, "name": catalog_list[catalog_id]["title"],
                                  "country_id": country_id})
        self._databse_connector.insert_catalog(database_list)

    def _process_categories(self, category_list: list):
        database_list = []
        for category in category_list:
            if category["id"] != "op_datpro":
                # Chance for different search API versions
                database_list.append({"category_id": category["id"].lower(), "name": category["title"]["en"]})
        self._databse_connector.insert_category(database_list)

    def _process_datasets_per_category(self, category_metadata: dict):
        self._log.info("Processing datasets per category.")
        database_list = []
        for category_id in category_metadata.keys():
            if category_id != "op_datpro":
                database_list.append({"date": date.today(), "category_id": category_id.lower(),
                                      "value": category_metadata[category_id]["count"]})
        self._databse_connector.insert_dataset_per_category(database_list)

    def _process_datasets_per_catalogue(self, catalog_metadata: dict):
        self._log.info("Processing datasets per catalogue.")
        self._log.debug(json.dumps(catalog_metadata))
        database_list = []
        for catalog_id in catalog_metadata.keys():
            database_list.append({"date": date.today(), "catalog_id": catalog_id,
                                  "value": catalog_metadata[catalog_id]["count"]})
        self._databse_connector.insert_dataset_per_catalog(database_list)

    def _process_dataset_per_country(self, country_metadata: dict):
        self._log.info("Processing datasets per country")
        database_list = []
        for country_id in country_metadata.keys():
            database_list.append({"date": date.today(), "country_id": country_id,
                                  "value": country_metadata[country_id]["count"]})
        self._databse_connector.insert_dataset_per_country(database_list)

    def _process_dataset_per_country_and_catalogue(self, catalog_metadata: dict):
        self._log.info("Processing datasets per country and catalogue")
        database_list = []
        for catalog_id in catalog_metadata.keys():
            if catalog_metadata[catalog_id]["country"] is not None:
                database_list.append({"date": date.today(), "country_id": catalog_metadata[catalog_id]["country"],
                                      "catalog_id": catalog_id, "value": catalog_metadata[catalog_id]["count"]})
        self._databse_connector.insert_dataset_per_country_and_catalog(database_list)

    def _process_dataset_per_country_and_category(self, country_metadata: dict):
        self._log.info("Processing datasets per country and category.")
        database_list = []
        self._log.info(country_metadata)
        for country_id in country_metadata.keys():
            for category_id in country_metadata[country_id]:
                if category_id == "count" or category_id == "title":
                    continue
                self._insert_categories(category_id)
                database_list.append({"date": date.today(), "country_id": country_id,
                                      "category_id": category_id.lower(), "value": country_metadata[country_id][category_id]})
        self._databse_connector.insert_dataset_per_country_and_category(database_list)
        
    def _insert_categories(self, category_id):
        name_from_category_keyword = Category.get_name_from_category_keyword(category_id)
        if name_from_category_keyword is not None:
            database_list = []
            database_list.append({"category_id": category_id, "name": name_from_category_keyword})
        self._databse_connector.insert_category(database_list)

    def _process_assigned_datasets_to_categories_per_contry(self, country_metadata: dict):
        self._log.info("Processing assigned datasets to categories per country.")
        database_list = []
        for country_id in country_metadata.keys():
            metadata = country_metadata[country_id]
            assigned_datasets = 0
            for category_id in metadata.keys():
                if category_id == "count" or category_id == "title":
                    continue
                assigned_datasets += metadata[category_id]
            database_list.append({"date": date.today(), "country_id": country_id,
                                  "assigned_dataset": assigned_datasets, "total_dataset": metadata["count"]})
        self._databse_connector.insert_dataset_assigned_to_category(database_list)

    def _process_assigned_datasets_to_categories_per_country_and_catalogue(self, catalog_metadata: dict):
        self._log.info("Processing assigend datasets to categories per country and catalogue.")
        database_list = []
        for catalog_id in catalog_metadata.keys():
            metadata = catalog_metadata[catalog_id]
            assigned_dataset = 0
            if metadata["country"] is not None:
                for category_id in metadata.keys():
                    if category_id == "count" or category_id == "title" or category_id == "country":
                        continue
                    assigned_dataset += metadata[category_id]
                database_list.append({"date": date.today(), "country_id": metadata["country"],
                                      "catalog_id": catalog_id, "assigned_dataset": assigned_dataset,
                                      "total_dataset": metadata["count"]})
        self._databse_connector.insert_dataset_assigned_to_country_and_catalog(database_list)

    def _process_number_of_all_datasets(self, datasets: int):
        dc = DatabaseConnector()
        dc.insert_dataset([{"date": date.today(), "value": datasets}])

    def _assign_values(self, res, key, value) -> dict:
        data = {}
        if len(res) > 0:
            for r in res["result"]:
                data[r[key]] = r[value]
        return data

    def _get_title_of_catalog(self, catalog_id: str, title: dict, country_id: str) -> str:
        if title is None:
            return catalog_id
        elif "en" in title.keys():
            return title["en"]
        elif country_id in title.keys():
            return title[country_id]
        else:
            return catalog_id

    def _build_sum(self, data, package_name, counter_name) -> int:
        try:
            sum = 0
            for entry in data[package_name]:
                sum += entry[counter_name]
        except KeyError:
            sum = 'NA'
        return sum

    def _send_post_request(self, url: str, json_body: json) -> dict:
        try:
            header = {"Content-Type": "application/json"}
            if self._is_auth_required == "true":
                r = requests.post(url, data=json_body, headers=header, timeout=360,
                                  auth=requests.auth.HTTPBasicAuth(self._hub_user, self._hub_password))
            else:
                self._log.info("url:"+url)
                self._log.info("data:"+json_body)
                r = requests.post(url, data=json_body, headers=header, timeout=360)
                self._log.info("Response:")
                self._log.info(r)
            return r.json()
        except (TimeoutError, requests.exceptions.ConnectionError):
            self._log.error("Connection error occured.")
            return {}
        except json.decoder.JSONDecodeError:
            self._log.error("Json decoding error occured.")
            return {}

    def _send_get_request(self, url: str) -> dict:
        try:
            if self._is_auth_required == "true":
                r = requests.get(url, timeout=120, auth=requests.auth.HTTPBasicAuth(self._hub_user, self._hub_password))
            else:
                r = requests.get(url, timeout=120)
            return r.json()
        except (TimeoutError, requests.exceptions.ConnectionError):
            self._log.error("Connection error occured.")
            return {}
        except json.decoder.JSONDecodeError:
            self._log.error("Json decoding error occured.")
            return {}
