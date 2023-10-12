import logging
import sys
import json
from flask import Flask, send_from_directory, request, Response
from flask_cors import CORS
from sqlalchemy.exc import ProgrammingError
from util.app_configuration import AppConfiguration
from data_store.database_connector import DatabaseConnector
from network.edp_request_sender import EdpRequestSender
from network.upload_api_connector import UploadApiConnector
from xlsx.xlsx_handler import XlsxHandler
from web.json_handler import JsonHandler
from util.identifier import Identifier
from request_scheduler import RequestScheduler

# Read system configuration
conf = AppConfiguration()
conf.read_config("../application_config.ini")

# Initialise Logging-System (-> stdout / stderr)
logging.basicConfig(level=logging.DEBUG, stream=sys.stdout,
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
log = logging.getLogger("Main")
log.info("Configuration and Logger initialised.")

# Flask initialization
app = Flask(__name__)
CORS(app)
# CORS(app, resources={r"/*": {"origins": r"*fokus.fraunhofer.de"}})
log.info("Flask App initialised.")

# Initialse database
db = DatabaseConnector()
db.create_database()
log.info("Database initialised.")


# Initialise request scheduler
scheduler = RequestScheduler()
scheduler.start()


@app.route('/')
def hello_service():
    return Response('EDP Statistics Service. Service is online.', 200, mimetype='text/plain')


@app.route('/xlsx', methods=['GET', 'DELETE'])
def get_xlsx_file():
    if request.method == 'GET':
        ip = request.remote_addr
        start_date = request.args.get('startdate')
        end_date = request.args.get('enddate')
        interval = request.args.get('interval')
        try:
            interval = int(interval)
        except TypeError:
            interval = 1
        xlsx = XlsxHandler()
        filename = xlsx.create_xlsx_file(ip, start_date, end_date, interval)
        return send_file_to_clint(filename)
    elif request.method == 'DELETE':
        xlsx = XlsxHandler()
        xlsx.delete_xlsx()
        return Response('Deletion of all xlsx files successful.', 200, mimetype="text/plain")


@app.route('/data', methods=['GET'])
def help_with_data():
    return Response(json.dumps(_build_json_for_unkown_identifier(200, "See list of available identifiers.")), 200,
                    mimetype="application/json")


@app.route('/data/<identifier>', methods=['GET'])
def get_data(identifier: str):
    if identifier not in Identifier.get_all_identifier():
        return Response(json.dumps(_build_json_for_unkown_identifier(400, "Identifier unknown.")), 400,
                        mimetype="application/json")
    json_handler = JsonHandler()
    try:
        if identifier in Identifier.get_all_identifier_keywords():
            if bool(request.args.get("list")):
                json_data = json_handler.create_json(identifier)
                status_code = 200
            else:
                json_data = json_handler.create_json(identifier)
                status_code = 200
    except (ProgrammingError, IndexError):
        json_data = _build_error_json()
        status_code = 404
    return Response(json_data, status_code, mimetype="application/json")


@app.route('/grab-data')
def initialize_data_harvesting():
    start_edp_request()
    return Response("Manually request to EDP started.", 200, mimetype="text/plain")


@app.route('/test')
def test_case():
    upload = UploadApiConnector()
    filename = upload.create_xlsx_file()
    status = upload.prepare_upload(filename)
    log.debug("Prepare upload finished with status {}".format(status))
    if status == 200:
        log.info("Data upload prepared successful.")
        upload_status = upload.send_xlsx_to_hub(filename)
        if upload_status == 200:
            log.info("Data upload successful.")
            upload.add_distribution_to_dataset(filename)
            return Response("Upload successfull", 200, mimetype="text/plain")
        else:
            return Response("Upload failed", 500, mimetype="text/plain")
    else:
        return Response("Upload prepare failed with status code %s" % status, 500, mimetype="text/plain")


def send_file_to_clint(filename):
    return send_from_directory(conf.resource_path, filename, as_attachment=True)


def _build_json_for_unkown_identifier(statuscode, message) -> dict:
    identifier_list = []
    for item in Identifier.get_all_identifier().keys():
        identifier_list.append(item)
    return {
        "statusCode": statuscode,
        "message": message,
        "available_identifier": identifier_list
    }


def _build_error_json() -> json:
    return json.dumps({"statusCode": 404, "message": "Data not available."})


def start_edp_request():
    request_sender_thread = EdpRequestSender()
    request_sender_thread.start()
