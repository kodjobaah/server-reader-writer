package com.txodds

import java.io._
import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import org.zeromq.ZMQ

/**
  * Created by baahko01 on 02/03/2017.
  */
class WriterOperations(context: ZMQ.Context, socket: ZMQ.Socket) extends Thread {

  override def run() {

    println("----sending stuff")
    socket.send("kodjo".getBytes, "kodjo".getBytes.length)
    var message: List[String] = List()
    while (!Thread.currentThread.isInterrupted) {
      message = retrieveRequest(socket)
      if (!message.isEmpty) {
        performOperation(message, socket)
      }

    }
    socket.close()

  }


  def performOperation(message: List[String], socket: ZMQ.Socket) {
    var identifier: String = message.head

    println("wriet["+message+"]")

  }

  def retrieveRequest(socket: ZMQ.Socket): List[String] = {
    var message: List[String] = List()
    var more: Boolean = false
    var reply: Array[Byte] = Array[Byte]()
    do {
      reply = socket.recv()
      if (reply != null) {
        val mess = new String(reply, Charset.forName("UTF-8"))
        message = message ::: List(mess)
      } else {
      }
      more = socket.hasReceiveMore
    } while (more)
    message
  }

}

