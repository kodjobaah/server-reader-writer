package com.txodds.helper

/**
 * Created by baahko01 on 05/03/2017.
 */
object ReaderHelper {

  def findOutSequence(cur: List[String], prevSequence: Option[Int]): List[String] = {
    if (cur.nonEmpty) {
      val value = cur.head.split(",")
      val curSequence = value(1).substring(0, value(1).length - 1).trim.toInt
      if (prevSequence.nonEmpty) {
        if ((prevSequence.get + 1) != curSequence) {
          findOutSequence(cur.tail, Option(curSequence)) ++ List(cur.head)
        } else {
          findOutSequence(cur.tail, Option(curSequence))
        }
      } else {
        findOutSequence(cur.tail, Option(curSequence))
      }
    } else {
      List.empty[String]
    }
  }
}
