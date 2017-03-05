package com.txodds

import java.nio.charset.Charset

import com.google.common.collect.EvictingQueue
import com.txodds.helper.ReaderHelper
import com.txodds.montior.{ JMXMonitoring, ReaderJmx, ReaderMonitor }
import org.slf4j.{ Logger, LoggerFactory }
import org.zeromq.ZMQ

class ReaderOperations(context: ZMQ.Context, socket: ZMQ.Socket) extends Thread {

  val log: Logger = LoggerFactory.getLogger(classOf[ReaderOperations])
  val monitorInfo = new ReaderJmx
  val jmxMonitoring = new JMXMonitoring("reader")
  jmxMonitoring.register(monitorInfo)

  @volatile var q = EvictingQueue.create[String](1000)

  def sendStartMessage(): Unit = {
    val uuid = java.util.UUID.randomUUID.toString
    val requestMessage = s"""($uuid, 0)"""
    log.debug("sending message[" + requestMessage + "]")
    socket.send(requestMessage, 0)
  }

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

    val msg = message.reverse.tail.reverse

    log.debug("count[" + msg.length + "] content[" + msg.mkString(","))

    ReaderMonitor.updateTotals(msg.length)
    ReaderMonitor.updateInflight(q.size())

    val itemsOutOfSequence = ReaderHelper.findOutSequence(msg, Option.empty[Int])

    if (itemsOutOfSequence.nonEmpty) {
      log.info("{} Items were out of sequence - here are the items {}", itemsOutOfSequence.size, itemsOutOfSequence.mkString(","))
    }

    itemsOutOfSequence.map(item => ReaderMonitor.updateOutofSequence(item))

    msg.foreach(q.add(_))

    val uuid = java.util.UUID.randomUUID.toString
    val requestMessage = s"""($uuid, 0)"""
    socket.send(requestMessage)

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
      }
      more = socket.hasReceiveMore
    } while (more)
    message
  }

}
