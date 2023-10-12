import schedule
import time
import logging
from datetime import datetime
from threading import Thread
from network.edp_request_sender import EdpRequestSender
from xlsx.xlsx_handler import XlsxHandler


class RequestScheduler(Thread):
    def __init__(self):
        Thread.__init__(self)
        self._log = logging.getLogger("RequestScheduler")

    def run(self):
        self._log.info("Scheduler is set to 1st of month at 09:30")
        schedule.every().hour.do(self._clear_received_requests)
        while True:
            self._log.debug(str(datetime.now()))
            if str(datetime.now())[11:16] == '10:26':
                schedule.run_pending()
                self._log.info("Scheduler initialize statistic generating now.")
                self._start_requests_to_edp()
                time.sleep(60)
            time.sleep(45)

    def _start_requests_to_edp(self):
        edp_request_sender = EdpRequestSender()
        edp_request_sender.start()

    def _clear_received_requests(self):
        xlsx_handler = XlsxHandler()
        xlsx_handler.delete_xlsx()
