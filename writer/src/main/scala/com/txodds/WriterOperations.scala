package com.txodds

import java.io._
import java.nio.charset.Charset
import java.nio.file.{ Files, Paths }
import org.zeromq.ZMQ

/**
 * Created by baahko01 on 02/03/2017.
 */
class WriterOperations(context: ZMQ.Context, socket: ZMQ.Socket) extends Thread {

  val r = scala.util.Random
  var counter = 1
  override def run() {

    println("----sending stuff")
    socket.send("0")
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
    var request: String = message.head

    val parts = request.split(",")

    println("message writer received[" + message + "]" + counter)
    println(parts(0).substring(1, parts(0).length).trim())
    println(parts(1).substring(0, parts(1).length - 1).trim())
    val start = parts(0).substring(1, parts(0).length).trim().toInt
    val end = parts(1).substring(0, parts(1).length).trim().toInt

    for (i <- start until (start + (end - 1))) {
      socket.send(i.toString, ZMQ.SNDMORE)
    }
    socket.send((start + end).toString)
  }

  def retrieveRequest(socket: ZMQ.Socket): List[String] = {
    var message: List[String] = List()
    var more: Boolean = false
    var reply: Array[Byte] = Array[Byte]()
    do {
      println("writer waiting")
      reply = socket.recv()
      println("writer-reiceve")
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

