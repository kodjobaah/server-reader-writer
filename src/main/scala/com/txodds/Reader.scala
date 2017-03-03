package com.txodds

import org.zeromq.ZMQ

/**
  * Created by baahko01 on 02/03/2017.
  */
class Reader(context: ZMQ.Context) extends Thread {

  override def run() {
    //  Setting up the proxy
    val frontend: ZMQ.Socket = context.socket(ZMQ.ROUTER)
    val backend: ZMQ.Socket = context.socket(ZMQ.DEALER)
    frontend.bind("tcp://*:12345")
    backend.bind("inproc://backend")

    val monitorName = "reader"
    frontend.monitor("inproc://monitor.socket."+monitorName, ZMQ.EVENT_ALL)


    val monitor = new MonitorConnection(context, "reader", monitorName)
    monitor.start()

    ZMQ.proxy(frontend, backend, null)

    frontend.close()
    backend.close()
  }
}
