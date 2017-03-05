package com.txodds.montior

import java.lang.management.ManagementFactory
import javax.management.{ MBeanServer, ObjectName }

/**
 * Created by baahko01 on 04/03/2017.
 */

object JMXMonitoring {

  trait ReaderJmxMXBean {
    def completedSequences: Int
    def inflightSequences: Int
    def outOfSequence: String
  }

}
class JMXMonitoring(item: String) {

  import JMXMonitoring._
  private val objectName = new ObjectName("com.txodds.monitor." + item + ":type=" + item)
  private val server = ManagementFactory.getPlatformMBeanServer

  def register(mbean: ReaderJmxMXBean) = {
    server.registerMBean(mbean, objectName)
  }
}
