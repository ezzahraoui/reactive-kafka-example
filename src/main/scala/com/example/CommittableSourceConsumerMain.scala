package com.example

import akka.actor._
import kafka._

object CommittableSourceConsumerMain extends App {
	val system = ActorSystem("CommittableSourceConsumerMain")
	system.actorOf(CommittableSourceConsumerActor.props, "CommittableSourceConsumerActor")
}