package org.csrj.coddingchallenge

import java.util.Properties

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.test.ConsumerRecordFactory
import org.csrj.coddingchallenge.avro.Serde
import org.scalatest.{FlatSpec, Matchers}

class HarmonizeTest extends FlatSpec with Matchers {

  "buildStream" should "retrun a stream that consume generic record ans produce generic record" ignore {


    val schemaRegistryUrl = "http://fake:1234"
    val stream: Topology = Harmonize.buildStream(schemaRegistryUrl)

    import org.apache.kafka.streams.{StreamsConfig, TopologyTestDriver}
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
    val testDriver = new TopologyTestDriver(stream, props)

    val keySerde = Serde.genericAvroSerde(schemaRegistryUrl, true)
    val valueSerde = Serde.genericAvroSerde(schemaRegistryUrl, false)
    val factory: ConsumerRecordFactory[GenericRecord, GenericRecord] = new ConsumerRecordFactory("input-topic", keySerde.serializer(), valueSerde.serializer());
    val schemaInputValue: Schema = new Schema.Parser().parse(
      """
        |{"namespace": "a.namespace",
        | "type": "record",
        | "name": "Status",
        | "fields": [
        |     {"name": "Id", "type": "long"},
        |     {"name": "Text", "type": "string"}
        | ]
        |}
      """.stripMargin)
    val schemaInputKey: Schema = new Schema.Parser().parse(
      """
        |{"namespace": "a.namespace",
        | "type": "record",
        | "name": "StatusKey",
        | "fields": [
        |     {"name": "Id", "type": "long"}
        | ]
        |}
      """.stripMargin)
    val recordValue = new GenericData.Record(schemaInputValue)
    recordValue.put("Id", 1)
    recordValue.put("Text", "This is a tweet.")

    val recordKey = new GenericData.Record(schemaInputKey)
    recordKey.put("Id", 1)

    testDriver.pipeInput(factory.create("twitter_status", recordKey, recordValue))

    val outputRecord = testDriver.readOutput("harmonized_status", keySerde.deserializer(), valueSerde.deserializer())
    outputRecord.key().get("id") shouldBe 1
    outputRecord.value().get("content") shouldBe "This is a tweet."
  }

}
