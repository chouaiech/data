import os
import json
import logging
import secrets
import requests
import calendar
from datetime import date
from requests_toolbelt import MultipartEncoder
from xlsx.xlsx_handler import XlsxHandler


class UploadApiConnector:

    def __init__(self):
        self._log = logging.getLogger(UploadApiConnector.__name__)
        self._file_upload_url = os.environ.get("FILE_UPLOAD_API")
        self._file_upload_auth = os.environ.get("FILE_UPLOAD_AUTH")
        self._file_upload_id = secrets.token_urlsafe(16)
        self._file_upload_token = secrets.token_urlsafe(8)
        self._dist_url = os.environ.get("DIST_API")
        self._dist_auth = os.environ.get("DIST_API_AUTH")
        self._download_url = os.environ.get("DOWNLOAD_URL")

    def create_xlsx_file(self):
        xlsx_handler = XlsxHandler()
        # filename = xlsx_handler.create_xlsx_file("localhost", start_date=str(date.today()), end_date=None, interval=1)
        filename = xlsx_handler.create_xlsx_file("localhost", start_date="2020-05-29", end_date=None, interval=1)
        filename = self._rename_file(filename)
        self._log.debug("Automated created file: %s" % str(filename))
        return filename

    def prepare_upload(self, filename):
        header = {"Content-Type": "application/json", "Authorization": self._file_upload_auth}
        body = [{"id": filename[:-5], "token": self._file_upload_token}]
        url = self._file_upload_url + "/v1/data"

        self._log.info("Prepare upload with: %s" % body)
        r = requests.put(url=url, data=json.dumps(body), headers=header)
        return r.status_code

    def send_xlsx_to_hub(self, filename):
        xlsx_path = "./resources/" + filename
        url = self._file_upload_url + "/v1/data/" + filename[:-5] + "?token=" + self._file_upload_token
        m = MultipartEncoder({"field": (filename, open(xlsx_path, "rb"), "multipart/form-data")})
        header = {"Content-Type": m.content_type}
        r = requests.post(url=url, headers=header, data=m)
        return r.status_code

    def add_distribution_to_dataset(self, filename):
        header = {"Content-Type": "text/turtle", "Authorization": self._dist_auth}
        url = self._dist_url + "/distributions?dataset=title330315468308&catalogue=upload-testing"
        download_url = self._download_url + "/" + filename[:-5]
        datestamp = date.today()
        identifier = calendar.month_name[datestamp.month].lower() + str(datestamp.year)
        body = self._build_dist_body(download_url, datestamp.day, datestamp.month, datestamp.year, identifier)
        self._log.debug(body)

        # r = requests.post(url, headers=header, data=body)
        # self._log.debug(r.status_code)
        # self._log.debug(r.content)
        # self._log.debug(r.text)

    def _rename_file(self, filename):
        new_filename = filename[0:3] + filename[4:14] + filename[15:25] + ".xlsx"
        os.rename("./resources/" + filename, "./resources/" + new_filename)
        return new_filename

    def _build_dist_body(self, download_url, day, month, year, identifier):
        return """
        @prefix dcat: <http://www.w3.org/ns/dcat#> .
        @prefix dct:   <http://purl.org/dc/terms/> .

        <https://europeandataportal.eu/set/distribution/> a dcat:Distribution ;
            dcat:accessURL <{0}> ;
            dct:format "XLSX"  ;
            dct:license <http://europeandataportal.eu/ontologies/od-licenses#CC-BY4.0> ;
            dct:title "EDP Statistics {1} {2} {3} Raw Data" ;
            dct:identifier "{4}";
        """.format(download_url, str(day), calendar.month_name[month], str(year), identifier)
