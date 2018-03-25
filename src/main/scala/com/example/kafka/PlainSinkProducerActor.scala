package com.example.kafka

import akka.actor.{Actor, ActorLogging, Props}
import akka.stream.ActorMaterializer
import akka.kafka.ProducerSettings
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import akka.stream.scaladsl.Source
import akka.kafka.scaladsl.Producer
import org.apache.kafka.clients.producer.ProducerRecord

object PlainSinkProducerActor {
  def props: Props = Props(new PlainSinkProducerActor())
}

class PlainSinkProducerActor extends Actor with ActorLogging {

  implicit val materializer = ActorMaterializer()

  val producerSettings = ProducerSettings(context.system,
                                          new ByteArraySerializer,
                                          new StringSerializer)

  override def preStart = {
    log.info("Start producer actor")
    self ! "start"
  }

  override def postStop() = {
    log.info("Shutdown producer actor")
  }

  def receive = {
    case "start" =>
      val done = Source(1 to 5)
        .map(_.toString)
        .map { elem =>
          new ProducerRecord[Array[Byte], String]("topic1", elem)
        }
        .runWith(Producer.plainSink(producerSettings))
  }
}
