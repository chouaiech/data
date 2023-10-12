from collections import KeysView


class Identifier:
    DS_PER_CATEGORY: str = "ds-per-category"
    DS_PER_CATALOGUE: str = "ds-per-catalogue"
    DS_PER_COUNTRY: str = "ds-per-country"
    DS_PER_ISO_COUNTRY: str = "ds-per-iso-country"
    DS_PER_COUNTRY_AND_CATALOGUE: str = "ds-per-country-and-catalogue"
    DS_PER_COUNTRY_AND_CATEGORY: str = "ds-per-country-and-category"
    DS_ASSIGNED_TO_CATEGORY: str = "ds-assigned-to-category"
    DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY: str = "ds-assigned-to-country-and-category"
    NUM_DATASETS: str = "num-datasets"

    def __init__(self):
        pass

    @staticmethod
    def get_string_identifier_dict() -> dict:
        return {
            Identifier.DS_PER_CATEGORY: "Ds per Category",
            Identifier.DS_PER_CATALOGUE: "Ds per Catalogue",
            Identifier.DS_PER_COUNTRY: "Ds per Country",
            Identifier.DS_PER_COUNTRY_AND_CATALOGUE: "Ds per Country and Catalogue",
            Identifier.DS_PER_COUNTRY_AND_CATEGORY: "Ds per Country and Category",
            Identifier.DS_ASSIGNED_TO_CATEGORY: "Ds assigned to Category",
            Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY: "Ds assigned to Country and Category",
            Identifier.NUM_DATASETS: "Number of all datasets"
        }

    @staticmethod
    def get_string_identifier(identifier: str):
        return Identifier.get_string_identifier_dict()[identifier]

    @staticmethod
    def get_all_identifier() -> dict:
        all_identifier = Identifier.get_string_identifier_dict()
        all_identifier[Identifier.DS_PER_ISO_COUNTRY] = "Ds per Country (as ISO)"
        return all_identifier

    @staticmethod
    def get_identifier_description(identifier) -> str:
        if identifier == Identifier.DS_PER_CATEGORY:
            return "Number of Datasets per Category"
        elif identifier == Identifier.DS_PER_CATALOGUE:
            return "Number of Datasets per Catalogue"
        elif identifier == Identifier.DS_PER_COUNTRY:
            return "Number of Datasets per Country"
        elif identifier == Identifier.DS_PER_COUNTRY_AND_CATALOGUE:
            return "Number of Datasets per Country and Catalogue"
        elif identifier == Identifier.DS_PER_COUNTRY_AND_CATEGORY:
            return "Number of Datasets per Country and Category"
        elif identifier == Identifier.DS_ASSIGNED_TO_CATEGORY:
            return "Number of assigned Datasets to Data Cat per Country"
        elif identifier == Identifier.DS_ASSIGNED_TO_COUNTRY_AND_CATEGORY:
            return "Number of assigned Datasets to Data Cat per Country and Catalogue"

    @staticmethod
    def get_identifier_keywords() -> KeysView:
        return Identifier.get_string_identifier_dict().keys()

    @staticmethod
    def get_all_identifier_keywords() -> KeysView:
        return Identifier.get_all_identifier().keys()

    @staticmethod
    def get_name_from_identifier_keyword(keyword) -> str:
        return Identifier.get_string_identifier_dict().get(keyword)
