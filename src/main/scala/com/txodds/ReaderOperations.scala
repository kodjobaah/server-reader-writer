package com.txodds

import java.nio.charset.Charset

import org.zeromq.ZMQ

/**
  * Created by baahko01 on 02/03/2017.
  */
class ReaderOperations(context: ZMQ.Context) extends Thread {

    val socket = context.socket(ZMQ.PAIR)
    socket.connect("inproc://backend")
    socket.monitor("inproc://monitor.req", ZMQ.EVENT_ALL)


    override def run() {

      var message: List[String] = List()
      while (!Thread.currentThread.isInterrupted) {
        message = retrieveRequest(socket)
        if (!message.isEmpty) {
          performOperation(message, socket)
        }

      }

    }


    def performOperation(message: List[String], socket: ZMQ.Socket) {
      var identifier: String = message.head

      socket.send(identifier, ZMQ.SNDMORE)
      socket.send("TERMINATE")

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
