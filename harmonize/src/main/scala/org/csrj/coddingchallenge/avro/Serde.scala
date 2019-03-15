package org.csrj.coddingchallenge.avro

import java.util.Collections

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.{GenericAvroSerde, SpecificAvroSerde}

object Serde {

  def genericAvroSerde(schemaRegistryUrl:String, isKey:Boolean): GenericAvroSerde = {
    val gas = new GenericAvroSerde
    gas.configure(Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl), isKey)
    gas
  }

}
