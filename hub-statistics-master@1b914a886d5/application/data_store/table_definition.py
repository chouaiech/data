from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import Column, String, Date, Integer, ForeignKey, create_engine
from util.database import DatabaseUtil

Base = declarative_base()
du = DatabaseUtil()


class Dataset(Base):
    __tablename__ = "dataset"

    date = Column(Date, primary_key=True, nullable=False)
    value = Column(Integer, nullable=False)

    def __repr__(self):
        return "<Dataset(date={0}, value={1})>".format(self.date, self.value)


class Country(Base):
    __tablename__ = "country"

    country_id = Column(String, primary_key=True, nullable=False)
    name = Column(String, nullable=False)

    def __repr__(self):
        return "<Country(country_id={0}, name={1})>".format(self.country_id, self.name)


class Category(Base):
    __tablename__ = "category"

    category_id = Column(String, primary_key=True, nullable=False)
    name = Column(String, nullable=False)

    def __repr__(self):
        return "<Category(category_id={0}, name={1})>".format(self.category_id, self.name)


class Catalog(Base):
    __tablename__ = "catalog"

    catalog_id = Column(String, primary_key=True, nullable=False)
    name = Column(String, nullable=False)
    # country_id = Column(String, ForeignKey("country.country_id"), nullable=True)
    country_id = Column(String, nullable=True)

    # country = relationship(Country)

    def __repr__(self):
        return "<Catalog(catalog_id={0}, name={1}, country_id={2})>".format(self.catalog_id, self.name, self.country_id)


class DatasetPerCategory(Base):
    __tablename__ = "ds_per_category"

    date = Column(Date, primary_key=True, nullable=False)
    category_id = Column(String, ForeignKey("category.category_id"), nullable=False, primary_key=True)
    value = Column(Integer)

    category = relationship(Category)

    def __repr__(self):
        return "<DS per Category(date={0}, category_id={1}, value={2})>".format(self.date, self.category_id, self.value)


class DatasetPerCatalog(Base):
    __tablename__ = "ds_per_catalog"

    date = Column(Date, primary_key=True, nullable=False)
    catalog_id = Column(String, ForeignKey("catalog.catalog_id"), nullable=False, primary_key=True)
    value = Column(Integer)

    catalog = relationship(Catalog)

    def __repr__(self):
        return "<DS per Category(date={0}, catalog_id={1}, value={2})>".format(self.date, self.catalog_id, self.value)


class DatasetPerCountry(Base):
    __tablename__ = "ds_per_country"

    date = Column(Date, primary_key=True, nullable=False)
    # country_id = Column(String, ForeignKey("country.country_id"), nullable=False, primary_key=True)
    country_id = Column(String, nullable=False, primary_key=True)
    value = Column(Integer)

    # country = relationship(Country)

    def __repr__(self):
        return "<DS per Country(date={0}, country_id={1}, value={2})>".format(self.date, self.country_id, self.value)


class DatasetPerCountryAndCatalog(Base):
    __tablename__ = "ds_per_country_and_catalog"

    date = Column(Date, primary_key=True, nullable=False)
    # country_id = Column(String, ForeignKey("country.country_id"), nullable=False)
    country_id = Column(String, nullable=False)
    catalog_id = Column(String, ForeignKey("catalog.catalog_id"), nullable=False, primary_key=True)
    value = Column(Integer)

    # country = relationship(Country)
    catalog = relationship(Catalog)

    def __repr__(self):
        return "<DS per Country and Catalog(date={0}, country_id={1} catalog_id={2}, value={3})>" \
            .format(self.date, self.country_id, self.catalog_id, self.value)


class DatasetPerCountryAndCategory(Base):
    __tablename__ = "ds_per_country_and_category"

    date = Column(Date, primary_key=True, nullable=False)
    # country_id = Column(String, ForeignKey("country.country_id"), nullable=False, primary_key=True)
    country_id = Column(String, nullable=False, primary_key=True)
    category_id = Column(String, ForeignKey("category.category_id"), nullable=False, primary_key=True)
    value = Column(Integer)

    # country = relationship(Country)
    category = relationship(Category)

    def __repr__(self):
        return "<DS per Country and Category(date={0}, country_id={1} category_id={2}, value={3})>" \
            .format(self.date, self.country_id, self.category_id, self.value)


class DatasetAssignedToCategory(Base):
    __tablename__ = "ds_assigned_to_category"

    date = Column(Date, primary_key=True, nullable=False)
    # country_id = Column(String, ForeignKey("country.country_id"), nullable=False, primary_key=True)
    country_id = Column(String, nullable=False, primary_key=True)
    assigned_dataset = Column(Integer)
    total_dataset = Column(Integer)

    # country = relationship(Country)

    def __repr__(self):
        return "<DS assigned to Category(date={0}, country_id={1} assigned_ds={2}/{3})>" \
            .format(self.date, self.country_id, self.assigned_dataset, self.total_dataset)


class DatasetAssignedToCountryAndCatalog(Base):
    __tablename__ = "ds_assigned_to_country_and_category"

    date = Column(Date, primary_key=True, nullable=False)
    # country_id = Column(String, ForeignKey("country.country_id"), nullable=False, primary_key=True)
    country_id = Column(String, nullable=False, primary_key=True)
    catalog_id = Column(String, ForeignKey("catalog.catalog_id"), nullable=False, primary_key=True)
    assigned_dataset = Column(Integer)
    total_dataset = Column(Integer)

    # country = relationship(Country)
    catalog = relationship(Catalog)

    def __repr__(self):
        return "<DS assigned to Country and Category(date={0}, country_id={1}, catalog_id={2} assigned_ds={3}/{4})>" \
            .format(self.date, self.country_id, self.catalog_id, self.assigned_dataset, self.total_dataset)


# Creating all tables in database
Base.metadata.create_all(create_engine(du.build_engine()))
