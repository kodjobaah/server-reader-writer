package com.txodds

import org.slf4j.{ Logger, LoggerFactory }
import org.zeromq.ZMQ

/**
 * Created by kodjobaah on 13/06/2014.
 */
class ReaderMonitorConnection(context: ZMQ.Context, from: String, monitorName: String, readerOperations: ReaderOperations) extends Thread {

  val log: Logger = LoggerFactory.getLogger(classOf[ReaderMonitorConnection])
  val socket = context.socket(ZMQ.PAIR)
  assert(socket != null)

  val monitor = socket.connect("inproc://monitor.socket." + monitorName)

  override def run() {
    while (!Thread.currentThread.isInterrupted) {

      val zmqEvent: ZMQ.Event = ZMQ.Event.recv(socket)
      assert(zmqEvent != null)
      val event = zmqEvent.getEvent

      if (event == ZMQ.EVENT_LISTENING) {
        log.info("{} listening : {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_ACCEPTED) {
        log.info("{} event_accepted {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CONNECTED) {
        readerOperations.sendStartMessage()
        log.info("{} event_connected {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CONNECT_DELAYED) {
        log.info("{} event_connect_delayed {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CLOSE_FAILED) {
        log.info("{} event_closed_failed {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_CLOSED) {
        log.info("{} event_closed {}", Array(from, zmqEvent.getAddress): _*)
      } else if (event == ZMQ.EVENT_DISCONNECTED) {
        readerOperations.q.clear()
        log.info("{} event_disconnected {}", Array(from, zmqEvent.getAddress): _*)
      } else {
        log.info("{} event_done {} ", Array(from, zmqEvent.getAddress): _*)
      }
    }
  }

}

