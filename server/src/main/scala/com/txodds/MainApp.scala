package com.txodds

import java.util
import java.util.concurrent.LinkedBlockingQueue

import com.google.common.collect.{ EvictingQueue, Queues }
import org.zeromq.ZMQ

import scala.collection.immutable.Seq

/**
 * Created by baahko01 on 02/03/2017.
 */
object MainApp {

  def main(args: Array[String]): Unit = {
    val numbersQueue: LinkedBlockingQueue[Seq[Int]] = new LinkedBlockingQueue[Seq[Int]]
    val context: ZMQ.Context = ZMQ.context(1)
    val context2: ZMQ.Context = ZMQ.context(2)
    val server = new Server(context, numbersQueue)
    val readerServer = new ServerReader(context2, numbersQueue)

    sys.addShutdownHook({
      println("ShutdownHook called")
      context.term()
      server.interrupt()
      server.join
      readerServer.interrupt()
      readerServer.join
    })

    readerServer.start
    server.start

  }
}
