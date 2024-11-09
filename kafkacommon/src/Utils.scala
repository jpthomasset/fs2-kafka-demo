package kafkacommon

import cats.effect.IO
import fs2.*

object Utils {

  /** An infinite stream of incrementing long values starting from 0.
    */
  val longStream =
    Stream
      .iterate(0L)(_ + 1)
      .covary[IO]
}
