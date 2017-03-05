package com.txodds

import java.util.concurrent.LinkedBlockingQueue

import com.txodds.monitor.MonitorConnection
import org.zeromq.ZMQ

import scala.collection.immutable.Seq
/**
 * Created by baahko01 on 03/03/2017.
 */
class ServerReader(context: ZMQ.Context, numbersQueue: LinkedBlockingQueue[Seq[Int]]) extends Thread {

  override def run() {
    //  Setting up the proxy
    val frontend: ZMQ.Socket = context.socket(ZMQ.PAIR)
    val backend: ZMQ.Socket = context.socket(ZMQ.DEALER)
    frontend.bind("tcp://*:12346")
    backend.bind("inproc://backend")

    val monitorName = "server-reader"
    frontend.monitor("inproc://monitor.socket." + monitorName, ZMQ.EVENT_ALL)

    val server = new ServerReaderOperations(context, numbersQueue)
    server.start()

    val monitor = new MonitorConnection(context, "server-reader", monitorName)
    monitor.start()

    ZMQ.proxy(frontend, backend, null)

    frontend.close()
    backend.close()
  }
}
