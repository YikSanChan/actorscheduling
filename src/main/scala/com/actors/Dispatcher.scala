package com.actors

import java.util.concurrent.ForkJoinPool

import scala.concurrent.duration._

class Dispatcher(val executorService: ForkJoinPool) {
  val throughputDeadlineTime: Duration = Duration.Zero

  val isThroughputDeadlineTimeDefined = true

  val throughput: Int = 10

  def dispatch(receiver: ActorCell, invocation: Envelope) = {
    val mbox = receiver.mailbox
    mbox.enqueue(receiver, invocation)
    registerForExecution(mbox, true, false)
  }

  def registerForExecution(mbox: MailBox, hasMessageHint: Boolean, hasSystemMessageHint: Boolean) = {
    if (mbox.canBeScheduled() && mbox.isIdle) {
      if (mbox.setAsScheduled()) {
        executorService execute mbox
      }
    }
  }
}
