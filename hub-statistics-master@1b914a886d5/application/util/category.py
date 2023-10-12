from collections import KeysView


class Category:

    def __init__(self):
        pass

    @staticmethod
    def get_categories() -> dict:
        return {
            "envi": "Environment",
            "regi": "Regions and cities",
            "gove": "Government and public sector",
            "soci": "Population and society",
            "econ": "Economy and finance",
            "educ": "Education, culture and sport",
            "heal": "Health",
            "tech": "Science and technology",
            "tran": "Transport",
            "just": "Justice, legal system and public safety",
            "agri": "Agriculture, fisheries, forestry and food",
            "ener": "Energy",
            "intr": "International issues"
        }

    @staticmethod
    def get_catgory_keywords() -> KeysView:
        return Category.get_categories().keys()

    @staticmethod
    def get_name_from_category_keyword(keyword) -> str:
        return Category.get_categories().get(keyword)
