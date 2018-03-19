package com.example.kafka

import akka.actor._
import akka.kafka._
import akka.stream._
import org.apache.kafka.common.serialization._
import akka.stream.scaladsl._
import akka.kafka.scaladsl._
import org.apache.kafka.common._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object CommittableSourceConsumerActor {
  def props: Props = Props(new CommittableSourceConsumerActor())
}

class CommittableSourceConsumerActor extends Actor with ActorLogging {

  implicit val materializer = ActorMaterializer()

  val consumerSettings = ConsumerSettings(context.system,
                                          new ByteArrayDeserializer,
                                          new StringDeserializer)
    .withGroupId("CommittableSourceConsumer")

  override def preStart = {
    log.info("Start consumer actor")
    self ! "start"
  }

  override def postStop() = {
    log.info("Shutdown consumer actor")
  }

  def receive = {
    case "start" =>
      Consumer
        .committableSource(
          consumerSettings,
          Subscriptions.assignment(new TopicPartition("video2", 1)))
        .mapAsync(1) { msg =>
          log.info(s"CommittableSourceConsumer consume: $msg")
          msg.committableOffset.commitScaladsl()
        }
        .runWith(Sink.ignore)
        .recoverWith {
          case e =>
            log.error("Exception: {}", e)
            self ! "start"
            Future.successful(Unit)
        }
  }
}
