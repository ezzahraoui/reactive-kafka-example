package com.example

import akka.actor._
import kafka._

object PlainSinkProducerMain extends App {
  val system = ActorSystem("PlainSinkProducerMain")
  system.actorOf(PlainSinkProducerActor.props, "PlainSinkProducerActor")
}
