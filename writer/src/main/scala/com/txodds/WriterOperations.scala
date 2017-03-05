package com.txodds

import java.io._
import java.nio.charset.Charset
import java.nio.file.{ Files, Paths }

import org.slf4j.{ Logger, LoggerFactory }
import org.zeromq.ZMQ

/**
 * Created by baahko01 on 02/03/2017.
 */
class WriterOperations(context: ZMQ.Context, socket: ZMQ.Socket) extends Thread {

  val log: Logger = LoggerFactory.getLogger(classOf[WriterOperations])
  val r = scala.util.Random

  @volatile var begin: Boolean = false
  def sendStart(): Unit = {
    log.info("----sending from monitor")
    socket.send("0")
    begin = true
  }

  override def run() {

    //waiting for initial message to be sent
    while (!begin) {
      Thread.sleep(300)
    }

    log.info("----starting receive")
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

    log.debug("message writer received[ {} ]", message)
    log.debug("part-0[{}]", parts(0).substring(1, parts(0).length).trim())
    log.debug("part-1[{}]", parts(1).substring(0, parts(1).length - 1).trim())
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
      reply = socket.recv(ZMQ.NOBLOCK)
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

