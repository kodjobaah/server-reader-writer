package com.txodds

import org.zeromq.ZMQ

/**
 * Created by baahko01 on 02/03/2017.
 */
object MainApp {

  def main(args: Array[String]): Unit = {

    val context: ZMQ.Context = ZMQ.context(1)

    val reader = new Reader(context)

    sys.addShutdownHook({
      println("ShutdownHook called")
      context.term()
      reader.interrupt()
      reader.join
    })
    reader.start

  }
}
