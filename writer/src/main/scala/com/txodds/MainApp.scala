package com.txodds

import org.zeromq.ZMQ

/**
 * Created by baahko01 on 02/03/2017.
 */
object MainApp {

  def main(args: Array[String]): Unit = {

    val context: ZMQ.Context = ZMQ.context(1)

    val writer = new Writer(context)

    sys.addShutdownHook({
      println("ShutdownHook called")
      context.term()
      writer.interrupt()
      writer.join
    })

    writer.start
  }
}
