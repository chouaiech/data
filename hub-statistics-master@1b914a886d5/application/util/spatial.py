from collections import KeysView


class Spatial:

    def __init__(self):
        pass

    @staticmethod
    def get_spatial_data() -> dict:
        return {
            "at": "Austria",
            "be": "Belgium",
            "bg": "Bulgaria",
            "cy": "Cyprus",
            "cz": "Czechia",
            "de": "Germany",
            "dk": "Denmark",
            "ee": "Estonia",
            "es": "Spain",
            "fi": "Finland",
            "fr": "France",
            "gb": "United Kingdom",
            "gr": "Greece",
            "hr": "Croatia",
            "hu": "Hungary",
            "ie": "Ireland",
            "it": "Italy",
            "lt": "Lithuania",
            "lu": "Luxembourg",
            "lv": "Latvia",
            "mt": "Malta",
            "nl": "Netherlands",
            "pl": "Poland",
            "pt": "Portugal",
            "ro": "Romania",
            "se": "Sweden",
            "si": "Slovenia",
            "sk": "Slovakia",
            "ch": "Switzerland",
            "is": "Iceland",
            "li": "Liechtenstein",
            "md": "Moldova",
            "no": "Norway",
            "rs": "Serbia",
            "ua": "Ukraine"
        }

    @staticmethod
    def get_iso_data() -> dict:
        return {v:k for (k, v) in Spatial.get_spatial_data().items()}

    @staticmethod
    def get_spatial_keywords() -> KeysView:
        return Spatial.get_spatial_data().keys()

    @staticmethod
    def get_name_from_spatial_keyword(keyword) -> str:
        return Spatial.get_spatial_data().get(keyword)
