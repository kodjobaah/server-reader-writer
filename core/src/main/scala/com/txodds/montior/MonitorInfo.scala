package com.txodds.montior

import com.txodds.montior.JMXMonitoring.ReaderJmxMXBean

/**
 * Created by baahko01 on 05/03/2017.
 */
class ReaderJmx extends ReaderJmxMXBean {

  override def completedSequences: Int = {
    ReaderMonitor.totalSequence
  }

  override def inflightSequences: Int = {
    ReaderMonitor.inflight
  }
  override def outOfSequence: String = {
    ReaderMonitor.outOfSequenceHolder.mkString(",")
  }
}
