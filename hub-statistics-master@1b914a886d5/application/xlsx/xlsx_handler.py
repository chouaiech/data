import datetime
import logging
from util.app_configuration import AppConfiguration
from util.identifier import Identifier
from xlsx.xlsx_file_connector import XlsxFileConnector
from util.date_manipulation import DataManipulation
from data_store.database_handler import DatabaseHandler


class XlsxHandler:

    def __init__(self):
        self._log = logging.getLogger("XlsxHandler")
        self._config = AppConfiguration()
        self._xlsx = XlsxFileConnector()
        self._stat = DataManipulation()
        self._database_handler = DatabaseHandler()

    def create_xlsx_file(self, ip: str, start_date: str, end_date: str, interval: int):
        if start_date is None and end_date is None and interval is None:
            filename = self._create_from_all_data(ip)
        elif start_date is None and end_date is None and interval is not None:
            filename = self._create_from_all_data(ip, interval)
        elif start_date is not None and end_date is None and interval is None:
            filename = self._create_since_date(ip, start_date)
        elif start_date is not None and end_date is None and interval is not None:
            filename = self._create_since_date(ip, start_date, interval)
        elif start_date is None and end_date is not None and interval is None:
            filename = self._create_unitl_date(ip, end_date)
        elif start_date is None and end_date is None and interval is not None:
            filename = self._create_unitl_date(ip, end_date, interval)
        elif start_date is not None and end_date is not None and interval is None:
            filename = self._create(ip, start_date, end_date)
        elif start_date is not None and end_date is not None and interval is not None:
            filename = self._create(ip, start_date, end_date, interval)
        else:
            self._log.error("Unexpected request argument combination found!")
            filename = None
        return filename

    def delete_xlsx(self, filename=None):
        if filename is None:
            return self._xlsx.delete_all_xlsx_files()
        else:
            return self._xlsx.delete_specific_xlsx_file(filename)

    def _create(self, remote_ip, start_date, end_date, interval=1):
        identifiers = Identifier.get_identifier_keywords()
        filename = self._xlsx.create_file(remote_ip)
        for identifier in identifiers:
            if identifier is not Identifier.NUM_DATASETS:
                header = Identifier.get_identifier_description(identifier)
                self._log.info("Create worksheet for " + Identifier.get_string_identifier(identifier))
                data = self._database_handler.get_data_from_identifier(identifier, start_date, end_date)
                self._xlsx.create_worksheet(header, data, identifier, filename)
        return filename

    def _create_since_date(self, remote_ip, start_date, interval=1):
        return self._create(remote_ip, start_date, datetime.datetime.now().strftime("%Y-%m-%d"),
                            interval)

    def _create_unitl_date(self, remote_ip, end_date, interval=1):
        return self._create(remote_ip, "1970-01-01", end_date, interval)

    def _create_from_all_data(self, remote_ip, interval=1):
        return self._create(remote_ip, "1970-01-01", datetime.datetime.now().strftime("%Y-%m-%d"), interval)
