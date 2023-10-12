from threading import Thread
from network.edp_statistic_harvester import EdpStatisticHarvester


class EdpRequestSender(Thread):
    def __init__(self):
        Thread.__init__(self)

    def run(self):
        connector = EdpStatisticHarvester()
        connector.start_all_request_to_edp()
