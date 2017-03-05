package com.txodds

import org.zeromq.ZMQ
import com.txodds.monitor.MonitorConnection

class Writer(context: ZMQ.Context) extends Thread {

  override def run() {
    //  Setting up the proxy
    println("-----setting up writer")
    val frontend: ZMQ.Socket = context.socket(ZMQ.REQ)
    frontend.connect("tcp://127.0.0.1:12345")

    val monitorName = "writer"
    frontend.monitor("inproc://monitor.socket." + monitorName, ZMQ.EVENT_ALL)

    val writerOperations = new WriterOperations(context, frontend)
    writerOperations.start()

    val monitor = new WriterMonitorConnection(context, "writer", monitorName, writerOperations)
    monitor.start()

  }
}
