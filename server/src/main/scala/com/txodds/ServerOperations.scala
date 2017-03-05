package com.txodds

import java.nio.charset.Charset
import java.util.concurrent.LinkedBlockingQueue

import com.redis.RedisClient
import org.zeromq.ZMQ

import scala.collection.immutable.Seq

object ServerOperations {

  val TotalItemsReceivedFromWriter = "totalitemsreceivedfromwriter"
  val TotalItemsSentToReader = "totalitemsenttoreader"
  val WriterItems = "writeritems"
}
/**
 * Created by baahko01 on 03/03/2017.
 */
class ServerOperations(context: ZMQ.Context, numbersQueue: LinkedBlockingQueue[Seq[Int]]) extends Thread {

  import ServerOperations._
  val redis = new RedisClient("localhost", 6379)
  import java.util.concurrent.ThreadLocalRandom
  val r = scala.util.Random
  val socket = context.socket(ZMQ.PAIR)
  socket.connect("inproc://backend")
  socket.monitor("inproc://monitor.req", ZMQ.EVENT_ALL)

  override def run() {
    //Populate queue with data that has not been processed.
    val numberOfItems: Option[String] = redis.get(TotalItemsReceivedFromWriter)
    if (numberOfItems.isEmpty) {
      redis.set(TotalItemsReceivedFromWriter, 0)
    } else {
      val itemsReceived = numberOfItems.get.toInt
      val items = redis.get(TotalItemsSentToReader)

      val itemsSent = items.fold(0)(_.toInt)

      if (itemsReceived > itemsSent) {
        val listOfItems: Option[List[Option[String]]] = redis.lrange(WriterItems, itemsSent, itemsReceived)

        val itemsToQueue = if (!listOfItems.isEmpty) {
          listOfItems.get.flatten.map { element: String =>
            val r: Seq[Int] = element.split(",").map(_.toInt).toList
            r
          }
        } else {
          Seq.empty[Seq[Int]]
        }

        if (!itemsToQueue.isEmpty) itemsToQueue.map(element => numbersQueue.add(element))
        println("---number of items in qeue[" + numbersQueue.size() + "]")
      }
    }

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

    val response = s"""(${ThreadLocalRandom.current().nextInt(1, 200)}, ${ThreadLocalRandom.current().nextInt(1, 200)}"""
    if (!request.equalsIgnoreCase("0")) {
      val data = message.mkString(",")
      redis.rpush(WriterItems, data)
      redis.incr(TotalItemsReceivedFromWriter)
      numbersQueue.add(message.map(_.toInt))
    }
    socket.send(response)

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
      reply = socket.recv()
      if (reply != null) {
        val mess = new String(reply, Charset.forName("UTF-8"))
        message += mess
      } else {
      }
      more = socket.hasReceiveMore
    } while (more)
    message.toList
  }

}
