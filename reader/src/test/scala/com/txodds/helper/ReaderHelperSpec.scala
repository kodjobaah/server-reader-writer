package com.txodds.helper

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by baahko01 on 05/03/2017.
  */
class ReaderHelperSpec extends FlatSpec with Matchers{

  "When sequences are consecutive" should "return an empty list" in {

    val list = List("(uu1,1)","(uu1,2)", "(uu1,3)", "(uu1,4)")

    val results = ReaderHelper.findOutSequence(list,Option.empty[Int])
    results should have size(0)
  }

  "When sequences are not consecutive" should "return the invalid sequence" in {

    val list = List("(uu1,1)","(uu1,3)", "(uu1,4)", "(uu1,7)")

    val results = ReaderHelper.findOutSequence(list,Option.empty[Int])
    results should have length 2
    results should contain theSameElementsAs List("(uu1,3)","(uu1,7)" )
  }

}
