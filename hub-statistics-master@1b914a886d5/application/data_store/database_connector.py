import logging
import pandas as pd
from datetime import date
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import sessionmaker
from sqlalchemy import create_engine
from util.database import DatabaseUtil
from data_store.table_definition import Dataset, Country, Category, Catalog, DatasetPerCountry, DatasetPerCategory, \
    DatasetPerCatalog, DatasetPerCountryAndCatalog, DatasetPerCountryAndCategory, DatasetAssignedToCategory, \
    DatasetAssignedToCountryAndCatalog
from unicodedata import category


class DatabaseConnector:

    def __init__(self):
        self._log = logging.getLogger(DatabaseConnector.__name__)
        self._database_util = DatabaseUtil()
        self._engine = create_engine(self._database_util.build_engine())
        self._base = declarative_base()
        self._session_factory = sessionmaker()
        self._session_factory.configure(bind=self._engine)

    def create_database(self):
        self._base.metadata.create_all(self._engine)

    def insert_dataset(self, dataset_list: list):
        session = self._session_factory()
        for dataset in dataset_list:
            session.add(Dataset(date=dataset["date"], value=dataset["value"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert all datasets information")
            session.rollback()
        session.close()

    def select_dataset(self, start_date: date, end_date: date):
        session = self._session_factory()
        query = session.query(Dataset) \
            .filter(Dataset.date >= start_date) \
            .filter(Dataset.date <= end_date)
        df = pd.DataFrame()
        for row in query:
            df.at["count", str(row.date)] = row.value
        session.close()
        df = df.sort_index(axis=1)
        df.index.name = "Number of Datasets"
        return df

    def insert_countries(self, countries_list: list):
        session = self._session_factory()
        for country in countries_list:
            session.merge(Country(country_id=country["country_id"], name=country["name"]))
        session.commit()
        session.close()

    def insert_category(self, category_list: list):
        session = self._session_factory()
        for category in category_list:
            session.merge(Category(category_id=category["category_id"], name=category["name"]))
        session.commit()
        session.close()

    def insert_catalog(self, catalog_list: list):
        session = self._session_factory()
        for catalog in catalog_list:
            session.merge(Catalog(catalog_id=catalog["catalog_id"], name=catalog["name"],
                                  country_id=catalog["country_id"]))
        session.commit()
        session.close()

    def insert_dataset_per_catalog(self, ds_per_catalog_list: list):
        session = self._session_factory()
        for dataset in ds_per_catalog_list:
            session.add(DatasetPerCatalog(date=dataset["date"], catalog_id=dataset["catalog_id"],
                                          value=dataset["value"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets per catalog")
            session.rollback()
        session.close()

    def select_dataset_per_catalog(self, startdate: date, enddate: date):
        session = self._session_factory()
        query = session.query(DatasetPerCatalog, Catalog) \
            .filter(DatasetPerCatalog.date >= startdate) \
            .filter(DatasetPerCatalog.date <= enddate) \
            .join(Catalog, DatasetPerCatalog.catalog_id == Catalog.catalog_id)
        df = pd.DataFrame()
        for row in query:
            df.at[row.Catalog.name, str(row.DatasetPerCatalog.date)] = row.DatasetPerCatalog.value
        session.close()

        df = df.sort_index(axis=1)
        df = df.sort_index(axis=0)
        df.index.name = "Number of Datasets per Catalogue"
        return df

    def insert_dataset_per_category(self, ds_per_category_list: list):
        session = self._session_factory()
        for dataset in ds_per_category_list:
            session.add(DatasetPerCategory(date=dataset["date"], category_id=dataset["category_id"],
                                           value=dataset["value"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets per category")
            session.rollback()
        session.close()

    def select_dataset_per_category(self, startdate: date, enddate: date):
        session = self._session_factory()
        query = session.query(DatasetPerCategory, Category) \
            .join(Category, DatasetPerCategory.category_id == Category.category_id) \
            .filter(DatasetPerCategory.date >= startdate) \
            .filter(DatasetPerCategory.date <= enddate)
        df = pd.DataFrame()
        for row in query:
            df.at[row.Category.name, str(row.DatasetPerCategory.date)] = row.DatasetPerCategory.value
        session.close()

        df = df.sort_index(axis=1)
        df.index.name = "Number of Datasets per Category"
        return df

    def insert_dataset_per_country(self, ds_per_country_list: list):
        session = self._session_factory()
        for dataset in ds_per_country_list:
            session.add(DatasetPerCountry(date=dataset["date"], country_id=dataset["country_id"],
                                          value=dataset["value"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets per country")
            session.rollback()
        session.close()

    def select_dataset_per_country(self, start_date: date, end_date: date):
        session = self._session_factory()
        query = session.query(DatasetPerCountry, Country) \
            .join(Country, DatasetPerCountry.country_id == Country.country_id) \
            .filter(DatasetPerCountry.date >= start_date) \
            .filter(DatasetPerCountry.date <= end_date)
        df = pd.DataFrame()
        for row in query:
            df.at[row.Country.name, str(row.DatasetPerCountry.date)] = row.DatasetPerCountry.value
        session.close()

        df = df.sort_index(axis=1)
        df = df.sort_index(axis=0)
        df.index.name = "Number of Datasets per Country"
        return df

    def select_dataset_per_iso_country(self, start_date: date, end_date: date):
        session = self._session_factory()
        query = session.query(DatasetPerCountry, Country) \
            .join(Country, DatasetPerCountry.country_id == Country.country_id) \
            .filter(DatasetPerCountry.date >= start_date) \
            .filter(DatasetPerCountry.date <= end_date)
        df = pd.DataFrame()
        for row in query:
            country_values = str(row.DatasetPerCountry.country_id) + ": " + row.Country.name
            df.at[country_values, str(row.DatasetPerCountry.date)] = row.DatasetPerCountry.value
        session.close()

        df = df.sort_index(axis=1)
        df.index.name = "Number of Datasets per Country (ISO)"
        return df

    def insert_dataset_per_country_and_catalog(self, ds_per_country_and_catalog_list: list):
        session = self._session_factory()
        for dataset in ds_per_country_and_catalog_list:
            session.add(DatasetPerCountryAndCatalog(date=dataset["date"], country_id=dataset["country_id"],
                                                    catalog_id=dataset["catalog_id"], value=dataset["value"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets per country and catalog")
            session.rollback()
        session.close()

    def select_dataset_per_country_and_catalog(self, start_date: date, end_date: date):
        session = self._session_factory()
        query = session.query(DatasetPerCountryAndCatalog, Catalog) \
            .filter(DatasetPerCountryAndCatalog.date >= start_date) \
            .filter(DatasetPerCountryAndCatalog.date <= end_date) \
            .join(Catalog, DatasetPerCountryAndCatalog.catalog_id == Catalog.catalog_id)
        df = pd.DataFrame()
        for row in query:
            country_catalog = str(row.DatasetPerCountryAndCatalog.country_id).upper() + ": " + row.Catalog.name
            df.at[country_catalog, str(row.DatasetPerCountryAndCatalog.date)] = row.DatasetPerCountryAndCatalog.value
        session.close()
        df = df.sort_index(axis=1)
        df = df.sort_index(axis=0)
        df.index.name = "Number of Datasets per Country and Catalogue"
        return df

    def insert_dataset_per_country_and_category(self, ds_per_country_and_category_list: list):
        
        session = self._session_factory()
        for dataset in ds_per_country_and_category_list:
            self._log.debug("date/country_id/category_id/value:")
            self._log.debug(dataset["date"])
            self._log.debug(dataset["country_id"])
            self._log.debug(dataset["category_id"])
            self._log.debug(dataset["value"])
            #if dataset["value"] > 0:  # Check if dataset["value"] is not zero
            session.add(DatasetPerCountryAndCategory(date=dataset["date"], country_id=dataset["country_id"],
                                                     category_id=dataset["category_id"], value=dataset["value"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets per country and category")
            session.rollback()
        session.close()

    def select_dataset_per_country_and_category(self, start_date, end_end):
        session = self._session_factory()
        query = session.query(DatasetPerCountryAndCategory, Category) \
            .filter(DatasetPerCountryAndCategory.date >= start_date) \
            .filter(DatasetPerCountryAndCategory.date <= end_end) \
            .join(Category, DatasetPerCountryAndCategory.category_id == Category.category_id)
        df = pd.DataFrame()
        for row in query:
            country_category = str(row.DatasetPerCountryAndCategory.country_id).upper() + ": " + row.Category.name
            df.at[country_category, str(row.DatasetPerCountryAndCategory.date)] = row.DatasetPerCountryAndCategory.value
        session.close()
        df = df.sort_index(axis=1)
        df = df.sort_index(axis=0)
        df.index.name = "Number of Datasets per Country and Category"
        return df

    def insert_dataset_assigned_to_category(self, ds_assigned_to_category_list: list):
        session = self._session_factory()
        for dataset in ds_assigned_to_category_list:
            session.add(DatasetAssignedToCategory(date=dataset["date"], country_id=dataset["country_id"],
                                                  assigned_dataset=dataset["assigned_dataset"],
                                                  total_dataset=dataset["total_dataset"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets assigned to category")
            session.rollback()
        session.close()

    def select_dataset_assigned_to_category(self, start_date: date, end_date: date):
        session = self._session_factory()
        query = session.query(DatasetAssignedToCategory, Country) \
            .filter(DatasetAssignedToCategory.date >= start_date) \
            .filter(DatasetAssignedToCategory.date <= end_date) \
            .join(Country, DatasetAssignedToCategory.country_id == Country.country_id)
        df = pd.DataFrame()
        for row in query:
            country_label = str(row.DatasetAssignedToCategory.country_id).upper() + ": " + row.Country.name
            df.at[country_label, str(row.DatasetAssignedToCategory.date) + " 1"] = \
                row.DatasetAssignedToCategory.assigned_dataset
            df.at[country_label, str(row.DatasetAssignedToCategory.date) + " 2"] = \
                row.DatasetAssignedToCategory.total_dataset
            df.at[country_label, str(row.DatasetAssignedToCategory.date) + " 3"] = \
                row.DatasetAssignedToCategory.assigned_dataset / \
                row.DatasetAssignedToCategory.total_dataset
        session.close()
        df = df.sort_index(axis=1)
        df = df.sort_index(axis=0)
        df.index.name = "Number of assigned Datasets to Data Cat per Country"
        return df

    def insert_dataset_assigned_to_country_and_catalog(self, ds_assigned_to_country_and_category_list: list):
        session = self._session_factory()
        for dataset in ds_assigned_to_country_and_category_list:
            session.add(DatasetAssignedToCountryAndCatalog(date=dataset["date"],
                                                           country_id=dataset["country_id"],
                                                           catalog_id=dataset["catalog_id"],
                                                           assigned_dataset=dataset["assigned_dataset"],
                                                           total_dataset=dataset["total_dataset"]))
        try:
            session.commit()
        except IntegrityError:
            self._log.error("Could not insert datasets assigned to country and category")
            session.rollback()
        session.close()

    def select_dataset_assigned_to_country_and_catalog(self, start_date: date, end_date: date):
        session = self._session_factory()
        query = session.query(DatasetAssignedToCountryAndCatalog, Catalog) \
            .filter(DatasetAssignedToCountryAndCatalog.date >= start_date) \
            .filter(DatasetAssignedToCountryAndCatalog.date <= end_date) \
            .join(Catalog, DatasetAssignedToCountryAndCatalog.catalog_id == Catalog.catalog_id)
        df = pd.DataFrame()
        for row in query:
            country_catalog = str(row.DatasetAssignedToCountryAndCatalog.country_id).upper() + ": " + \
                              str(row.Catalog.name)
            df.at[country_catalog, str(row.DatasetAssignedToCountryAndCatalog.date) + " 1"] = \
                row.DatasetAssignedToCountryAndCatalog.assigned_dataset
            df.at[country_catalog, str(row.DatasetAssignedToCountryAndCatalog.date) + " 2"] = \
                row.DatasetAssignedToCountryAndCatalog.total_dataset
            try:
                df.at[country_catalog, str(row.DatasetAssignedToCountryAndCatalog.date) + " 3"] = \
                    row.DatasetAssignedToCountryAndCatalog.assigned_dataset / \
                    row.DatasetAssignedToCountryAndCatalog.total_dataset
            except ZeroDivisionError:
                df.at[country_catalog, str(row.DatasetAssignedToCountryAndCatalog.date) + " 3"] = 0
        session.close()
        df = df.sort_index(axis=1)
        df = df.sort_index(axis=0)
        df.index.name = "Number of assigned Datasets to Data Cat per Country and Catalogue"
        return df

    def get_data(self):
        session = self._session_factory()
        for tupel in session.query(Country).all():
            self._log.debug(tupel)
