import asyncio
from argparse import ArgumentParser

import aiokafka


async def send_message(bootstrap_server: str, topic: str, file_path: str) -> None:
    with open(file_path, mode="rb") as token:
        message = token.read()

    producer = aiokafka.AIOKafkaProducer(bootstrap_servers=bootstrap_server)
    await producer.start()
    try:
        await producer.send_and_wait(topic, message)
    finally:
        await producer.stop()


if __name__ == "__main__":
    parser = ArgumentParser(description="Send a message to a Kafka topic.")
    parser.add_argument("bootstrap", help="Kafka bootstrap server url")
    parser.add_argument("topic", help="The name of the topic to send the message to.")
    parser.add_argument("file", help="The file to send")
    args = parser.parse_args()

    asyncio.run(send_message(args.bootstrap, args.topic, args.file))
