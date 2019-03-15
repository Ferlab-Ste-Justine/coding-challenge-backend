package org.csrj.coddingchallenge

import java.time.Duration
import java.util.Properties

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.kstream.{Consumed, Produced}
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.csrj.coddingchallenge.Harmonize.schemaRegistryUrl
import org.csrj.coddingchallenge.avro.HarmonizedStatus._
import org.csrj.coddingchallenge.avro.Serde.genericAvroSerde


object Harmonize extends App {

  val bootstrapServers = args(0)
  val schemaRegistryUrl = args(1)

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "harmonize")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    p.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
    p
  }

  if (KafkaUtil.waitForTopicExists("twitter_status", config)) {

    val stream = buildStream(schemaRegistryUrl)

    val streams: KafkaStreams = new KafkaStreams(stream, config)
    streams.start()

    sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(10))
    }
  }


  def buildStream(schemaRegistryUrl: String): Topology = {
    val serdeValue: Serde[GenericRecord] = genericAvroSerde(schemaRegistryUrl, isKey = false)
    val serdeKey = genericAvroSerde(schemaRegistryUrl, isKey = true)
    implicit val consumed: Consumed[GenericRecord, GenericRecord] = Consumed.`with`(serdeKey, serdeValue)
    implicit val produced: Produced[GenericRecord, GenericRecord] = Produced.`with`(serdeKey, serdeValue)
    val builder = new StreamsBuilder()

    builder.stream[GenericRecord, GenericRecord]("twitter_status")
      .map { (key, status) =>
        (harmonizedKey(key), harmonizedStatus(status))
      }
      .to("harmonized_status")
    builder.build()
  }


}


