package com.txodds.montior

/**
 * Created by baahko01 on 05/03/2017.
 */
object ReaderMonitor {
  val outOfSequenceHolder = scala.collection.mutable.ListBuffer.empty[String]
  var totalSequence: Int = 0
  var inflight: Int = 0

  def updateOutofSequence(sequence: String): Unit = {
    outOfSequenceHolder ++ sequence
  }

  def updateTotals(total: Int): Unit = {
    totalSequence = totalSequence + total
  }

  def updateInflight(count: Int): Unit = {
    inflight = count
  }

}
