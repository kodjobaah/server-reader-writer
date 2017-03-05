package com.txodds

import org.slf4j.{ Logger, LoggerFactory }
import org.zeromq.ZMQ

/**
 * Created by kodjobaah on 13/06/2014.
 */
class WriterMonitorConnection(context: ZMQ.Context, from: String, monitorName: String, writerOperations: WriterOperations) extends Thread {

  val log: Logger = LoggerFactory.getLogger(classOf[WriterMonitorConnection])
  val socket = context.socket(ZMQ.PAIR)
  assert(socket != null)

  val monitor = socket.connect("inproc://monitor.socket." + monitorName)

  override def run() {
    while (true) {

      val zmqEvent: ZMQ.Event = ZMQ.Event.recv(socket)
      assert(zmqEvent != null)
      val event = zmqEvent.getEvent

      if (event == ZMQ.EVENT_LISTENING) {
        log.info("{} listening : {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_ACCEPTED) {
        log.info("{} event_accepted {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CONNECTED) {
        writerOperations.sendStart()
        log.info("{} event_connected {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CONNECT_DELAYED) {
        log.info("{} event_connect_delayed {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CLOSE_FAILED) {
        log.info("{} event_closed_failed {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CLOSED) {
        log.info("{} event_closed {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_DISCONNECTED) {
        log.info("{} event_disconnected {}", Array(from, zmqEvent.getAddress): _*)
      } else {
        log.info("{} event_done {} ", Array(from, zmqEvent.getAddress): _*)
      }
    }
  }

}

