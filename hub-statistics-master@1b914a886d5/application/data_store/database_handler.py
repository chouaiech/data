import logging
from util.identifier import Identifier
from data_store.database_connector import DatabaseConnector


class DatabaseHandler:

    def __init__(self):
        self._log = logging.getLogger(DatabaseHandler.__name__)

    def get_data_from_identifier(self, identifier, start_date, end_date):
        db = DatabaseConnector()
        if identifier == Identifier.DS_PER_CATEGORY:
            return db.select_dataset_per_category(start_date, end_date)
        elif identifier == Identifier.DS_PER_CATALOGUE:
            return db.select_dataset_per_catalog(start_date, end_date)
        elif identifier == Identifier.DS_PER_COUNTRY:
            return db.select_dataset_per_country(start_date, end_date)
        elif identifier == Identifier.DS_PER_COUNTRY_AND_CATALOGUE:
            return db.select_dataset_per_country_and_catalog(start_date, end_date)
        elif identifier == Identifier.DS_PER_COUNTRY_AND_CATEGORY:
            return db.select_dataset_per_country_and_category(start_date, end_date)
        elif identifier == Identifier.DS_ASSIGNED_TO_CATEGORY:
            return db.select_dataset_assigned_to_category(start_date, end_date)
        elif identifier == Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY:
            return db.select_dataset_assigned_to_country_and_catalog(start_date, end_date)
        elif identifier == Identifier.NUM_DATASETS:
            return db.select_dataset(start_date, end_date)
        elif identifier == Identifier.DS_PER_ISO_COUNTRY:
            return db.select_dataset_per_iso_country(start_date, end_date)
        else:
            self._log.warning("Given identifier not found.")