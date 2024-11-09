package kafkaconsumer

import cats.effect.*
import fs2.*
import fs2.kafka.*
import org.typelevel.log4cats.*
import org.typelevel.log4cats.slf4j.Slf4jFactory
import fs2.kafka.consumer.KafkaConsumeChunk.CommitNow

object Main extends IOApp.Simple {

  given LoggerFactory[IO] = Slf4jFactory.create[IO]

  val logger = LoggerFactory[IO].getLogger

  val consumerSettings =
    ConsumerSettings[IO, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Latest)
      .withBootstrapServers("localhost:9091")
      .withGroupId("test-group")

  val run: IO[Unit] =
    KafkaConsumer
      .stream(consumerSettings)
      .subscribeTo("topic-2p")
      .consumeChunk { chunk =>
        chunk
          .traverse(r => logger.info(s"Received: (${r.key}, ${r.value}) - p: ${r.partition}, offset: ${r.offset}"))
          .as(CommitNow)
      }

}
