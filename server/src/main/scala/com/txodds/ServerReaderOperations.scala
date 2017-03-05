package com.txodds

import java.nio.charset.Charset
import java.util
import java.util.concurrent.LinkedBlockingQueue

import com.redis.RedisClient
import org.zeromq.ZMQ

import scala.collection.immutable.Seq

/**
 * Created by baahko01 on 03/03/2017.
 */
class ServerReaderOperations(context: ZMQ.Context, numbersQueue: LinkedBlockingQueue[Seq[Int]]) extends Thread {

  var curList = Seq.empty[Int]
  val redis = new RedisClient("localhost", 6379)
  val r = scala.util.Random
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
    var request: String = message.head

    val parts = request.split(",")
    val uuid = parts(0).substring(1, parts(0).length).trim()
    val start = parts(1).substring(0, parts(1).length - 1).trim().toInt

    val items: Seq[Int] = if (curList.isEmpty) {
      numbersQueue.take()
    } else {
      curList
    }

    val (itemsToSend, leftOver) = items.splitAt(10)
    curList = leftOver

    itemsToSend.map { item =>
      val it = s"""($uuid, $item)"""
      socket.send(it, ZMQ.SNDMORE)
    }

    val end = s"""($uuid, -1)"""
    socket.send(end)
    redis.incr(ServerOperations.TotalItemsSentToReader)

    println("----message server reader operations recieve[" + message + "]")

    //socket.send("ok")
    /*
    socket.send(identifier, ZMQ.SNDMORE)
    socket.send("TERMINATE")
     */
  }

  def retrieveRequest(socket: ZMQ.Socket): List[String] = {
    import scala.collection.mutable.ListBuffer
    var message = new ListBuffer[String]()

    var more: Boolean = false
    var reply: Array[Byte] = Array[Byte]()
    do {
      println("+serverReader start" + reply)
      reply = socket.recv()
      if (reply != null) {
        println("+serverReader" + reply)
        val mess = new String(reply, Charset.forName("UTF-8"))
        println(mess)
        println(message)
        message += mess
      } else {
      }
      more = socket.hasReceiveMore
    } while (more)
    message.toList
  }

}
