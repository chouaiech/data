#!/usr/bin/env bash
set -e

if [ "$1" = 'DEV' ];
then
    echo "Running Development Server"
    source ~/.python_virtualenv/piveau_hub_statistics/bin/activate
    export FLASK_APP=statistic_service.py
    export FLASK_ENV=development
    export FLASK_DEBUG=1
    export PYTHONPATH=./application/
    cd ./application/
    exec python3 -m flask run --port 9090 --debugger --with-threads --no-reload
elif [ "$1" = 'DOCKER' ];
then
    echo "Running Production Server in Docker Container"
    export FLASK_ENV=production
    exec uwsgi --http :9090 --wsgi-file /application/statistic_service.py --callable app --enable-threads -b 32768
else
    echo "Running Production Server"
    source ~/.python_virtualenv/piveau_hub_statistics/bin/activate
    exec uwsgi --http :9090 --chdir ./application/ --wsgi-file ./statistic_service.py --callable app --enable-threads -b 32768
fi
