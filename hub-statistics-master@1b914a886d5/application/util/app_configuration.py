import configparser


# Defining a metaclass for AppConfiguration
class Singleton(type):
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super(Singleton, cls).__call__(*args, **kwargs)
        return cls._instances[cls]


class AppConfiguration(metaclass=Singleton):

    def __init__(self):
        self._logging_in_file: bool = None
        self._logfile: str = None
        self._logging_in_stdout: bool = None

        self._database_filepath: str = None

        self._resource_path: str = None

    def read_config(self, config_file: str):
        config = configparser.ConfigParser()
        config.read(config_file)

        self._logging_in_file = config["log"].getboolean("logging_in_file")
        self._logfile = config["log"]["logfile"]
        self._logging_in_stdout = config["log"].getboolean("logging_in_stdout")

        self._database_filepath = config["database"]["file"]
        self._resource_path = config["xlsx"]["resource_path"]

    def is_logging_in_file(self) -> bool:
        return self._logging_in_file

    def get_logfile(self) -> str:
        return self._logfile

    def is_logging_in_stdout(self) -> bool:
        return self._logging_in_stdout

    def get_database_filepath(self) -> str:
        return self._database_filepath

    def get_resource_path(self) -> str:
        return self._resource_path

    logging_in_file: bool = property(is_logging_in_file)
    logfile: str = property(get_logfile)
    logging_in_stdout: bool = property(is_logging_in_stdout)

    database_filepath: str = property(get_database_filepath)
    resource_path: str = property(get_resource_path)
