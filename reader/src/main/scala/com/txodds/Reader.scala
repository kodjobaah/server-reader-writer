package com.txodds

import org.zeromq.ZMQ
import com.txodds.monitor.MonitorConnection

/**
 * Created by baahko01 on 02/03/2017.
 */
class Reader(context: ZMQ.Context) extends Thread {

  override def run() {
    //  Setting up the proxy
    val frontend: ZMQ.Socket = context.socket(ZMQ.PAIR)
    frontend.connect("tcp://127.0.0.1:12346")

    val monitorName = "reader"
    frontend.monitor("inproc://monitor.socket." + monitorName, ZMQ.EVENT_ALL)

    val readerOperations = new ReaderOperations(context, frontend)
    val monitor = new ReaderMonitorConnection(context, "reader", monitorName, readerOperations)
    monitor.start()

    readerOperations.start()
  }
}
