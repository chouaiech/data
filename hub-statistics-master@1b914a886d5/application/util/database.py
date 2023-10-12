import os
import logging


class DatabaseUtil:

    def __init__(self):
        self._log = logging.getLogger(DatabaseUtil.__name__)
        self._driver = "postgresql"
        self._user = os.environ.get("DB_USER")
        self._password = os.environ.get("DB_PASSWORD")
        self._host = os.environ.get("DB_HOST", "localhost")
        self._port = os.environ.get("DB_PORT", "5432")
        self._db_name = os.environ.get("DB")
        self._use_docker = os.environ.get("DB_IN_DOCKER", "true")

    def build_engine(self):
        if self._use_docker == "true":
            return self._driver + "://" + self._user + ":" + self._password + "@" + self._host + "/" + self._db_name
        else:
            return self._driver + "://" + self._user + ":" + self._password + "@" + self._host + ":" + self._port + \
                   "/" + self._db_name
