package kafkaproducer

import scala.concurrent.duration.*

import cats.effect.*
import fs2.*
import fs2.kafka.KafkaProducer
import fs2.kafka.ProducerRecord
import fs2.kafka.ProducerSettings
import kafkacommon.Utils
import org.typelevel.log4cats.*
import org.typelevel.log4cats.slf4j.Slf4jFactory

object Main extends IOApp.Simple {

  given LoggerFactory[IO] = Slf4jFactory.create[IO]

  val logger = LoggerFactory[IO].getLogger

  val settings =
    ProducerSettings[IO, String, String]
      .withBootstrapServers("localhost:9091")

  val run: IO[Unit] =
    Utils.longStream
      .metered(1000.millisecond)
      .map { sid => ProducerRecord("topic-2p", s"key-$sid", s"value-$sid") }
      .evalTap(r => logger.info(s"Producing: (${r.key}, ${r.value})"))
      .chunks
      .through(KafkaProducer.pipe(settings))
      .compile
      .drain

}
